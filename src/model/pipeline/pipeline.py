from tracker import Tracker
from par import PAR
from look import LOOK

from typing import List
from dataclasses import dataclass
from collections import defaultdict

import pandas as pd
import numpy as np
import cv2

@dataclass
class INFO:
    id: int
    frame_in: int
    frame_out: int
    gender: str
    age: str
    attention: float # 관심도 정도. 아직 정해지지 않음
    staring_list: list

class Pipeline:
    def __init__(self):
        self.tracker = Tracker()
        self.par = PAR()
        self.look = LOOK()
        
    def detect(self, image_path):
        results = self.tracker.detect(image_path)
        return results
    
    def track(self, video_path):
        results = self.tracker.track(video_path)        
        return results
        
    def head_pose(self, image_array):
        ...

    def gaze(self, image_array):
        ...
    
    def run(self, video_path, visualize = False) -> List[INFO]:
        """
        비디오 입력을 받아, 모든 모델을 사용하여 백엔드에 넘겨줄 정보를 만드는 함수
        """
        # 1. 객체 추적
        info_list = []
        results = self.track(video_path)
        h, w, c = results[0].orig_img.shape
        image_size = (h, w)
        dtypes = {
            'frame_id': 'int64',
            'left': 'float64',
            'top': 'float64',
            'right': 'float64',
            'bottom': 'float64',
            'track_id': 'int64',
            'conf': 'float64',
        }
        df = pd.DataFrame(columns=['frame_id', 'left', 'top', 'right', 'bottom', 'track_id', 'conf']).astype(dtypes)
        for i, r in enumerate(results):
            boxes = r.boxes
            data  = boxes.data.cpu().numpy()
            if len(data) == 0:
                continue
            frame_ids = np.full((data.shape[0], 1), i)
            t = np.hstack((frame_ids, data[:,:-1]))
            df = pd.concat([df, pd.DataFrame(t, columns=df.columns).astype(dtypes)], ignore_index=True)
        
        # 객체 추적 결과를 바탕으로 각 사람의 속성, 관심도 예측하는 루프
        for track_id in df.track_id.unique():
            df_track = df.query(f'track_id == {track_id}')

            frame_in = int(df_track.frame_id.min())
            frame_out = int(df_track.frame_id.max())
            if frame_out - frame_in < 20: # 20 프레임 이하로 등장한 사람은 처리하지 않음
                continue

            ages = defaultdict(int)
            genders = defaultdict(int)
            staring_list = [0] * (frame_out - frame_in + 1)

            for i, row in df_track.iterrows():
                frame_id, left, top, right, bottom, track_id, _ = row
                frame_id = int(frame_id)
                top = round(top)
                left = round(left)
                right = round(right)
                bottom = round(bottom)
                
                frame = results[frame_id].orig_img
                src = frame[top:bottom, left:right]
                h, w, _ = src.shape
                if w < 10 or h < 10: # 탐지된 이미지가 너무 작은 경우 추론 X
                    continue

                out = self.par.predict(src) # 성별, 연령대 인식(PAR) 모델
                
                ages[out['age']] += 1
                genders[out['gender']] += 1
                
                # 관심도 측정 모델
                kps_idx = (results[frame_id].boxes.id == track_id).nonzero()
                kps = results[frame_id].keypoints.data[kps_idx].detach().cpu().numpy().squeeze(0)
                out_look = self.look.predict(kps, image_size)
                if out_look > 0.5:
                    staring_list[frame_id - frame_in] = 1
                
            age = max(ages.items(), key=lambda x:x[1])[0]
            gender = max(genders.items(), key=lambda x:x[1])[0]
            info = INFO(
                track_id,
                frame_in,
                frame_out,
                gender,
                age,
                sum(staring_list) / len(staring_list),
                staring_list
            )
            info_list.append(info)
            
        if visualize:
            frames = []
            for r in results:
                plotted = r.plot()
                frames.append(plotted)
            
            for info in info_list:
                track_id = info.id
                gender = info.gender
                staring_list = info.staring_list
                df_track = df.query(f'track_id == {track_id}')
                frame_in = int(df_track.frame_id.min())
                for i, row in df_track.iterrows():
                    frame_id, left, top, right, bottom, *_ = row
                    frame_id = int(frame_id)
                    top = round(top)
                    left = round(left)
                    right = round(right)
                    bottom = round(bottom)
                    GREEN = (0, 255, 0)
                    RED = (0, 0, 255)
                    cv2.putText(frames[frame_id], f'{gender}', (left, top+40), cv2.FONT_HERSHEY_SIMPLEX,
                                1, GREEN, 3, cv2.LINE_AA)
                    look = staring_list[frame_id - frame_in]
                    cv2.putText(frames[frame_id], f'{look}', (left, top+80), cv2.FONT_HERSHEY_SIMPLEX,
                                1, GREEN if look else RED, 3, cv2.LINE_AA)
            
            height, width, _ = frames[0].shape
            cap = cv2.VideoCapture(video_path)
            fps = cap.get(cv2.CAP_PROP_FPS)
            cap.release()
            fourcc = cv2.VideoWriter_fourcc(*'mp4v')
            out = cv2.VideoWriter('out-vis.avi', fourcc, fps, (width, height)) # TODO: 저장 경로 설정
            for i, frame in enumerate(frames):
                out.write(frame)
            out.release()

        return info_list
    
if __name__ == '__main__':
    pipeline = Pipeline()
    ret = pipeline.run('out.avi', visualize=True)
    print(ret)