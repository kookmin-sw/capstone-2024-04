from tracker import Tracker
from par import PAR

from typing import List
from dataclasses import dataclass
from collections import defaultdict

import pandas as pd
import numpy as np

@dataclass
class INFO:
    id: int
    frame_in: int
    frame_out: int
    gender: str
    age: str
    attention: float # 관심도 정도. 아직 정해지지 않음

class Pipeline:
    def __init__(self):
        self.tracker = Tracker()
        self.par = PAR()
        
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
    
    def run(self, video_path) -> List[INFO]:
        """
        비디오 입력을 받아, 모든 모델을 사용하여 백엔드에 넘겨줄 정보를 만드는 함수
        #TODO: 관심도 측정 기능 구현
        """
        info_list = []
        results = self.track(video_path)
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
        
        for track_id in df.track_id.unique():
            df_track = df.query(f'track_id == {track_id}')

            frame_in = int(df_track.frame_id.min())
            frame_out = int(df_track.frame_id.max())
            if frame_out - frame_in < 20:
                continue

            ages = defaultdict(int)
            genders = defaultdict(int)

            for i, row in df_track.iterrows():
                frame_id, left, top, right, bottom, track_id, _ = row
                top = round(top)
                left = round(left)
                right = round(right)
                bottom = round(bottom)
                
                frame = results[int(frame_id)].orig_img
                src = frame[top:bottom, left:right]
                h, w, _ = src.shape
                if w < 10 or h < 10: # 탐지된 이미지가 너무 작은 경우 추론 X
                    continue

                out = self.par.predict(src)
                
                ages[out['age']] += 1
                genders[out['gender']] += 1
                
            age = max(ages.items(), key=lambda x:x[1])[0]
            gender = max(genders.items(), key=lambda x:x[1])[0]
            info = INFO(
                track_id,
                frame_in,
                frame_out,
                gender,
                age,
                0
            )
            info_list.append(info)
        return info_list
    
if __name__ == '__main__':
    pipeline = Pipeline()
    ret = pipeline.run('out.avi')
    print(ret)