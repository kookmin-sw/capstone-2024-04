from collections import defaultdict

import cv2
import numpy as np
import pandas as pd
import torch

from fastapi import FastAPI, File, UploadFile
from mot import parse_opt, run
from par import Backbone_nFC, transforms

model = Backbone_nFC(30)
model.load_state_dict(torch.load('/home/kh/capstone/PAR/Person-Attribute-Recognition-MarketDuke/checkpoints/market/resnet50_nfc/net_last.pth'))
model.eval()

app = FastAPI()

@app.post('/upload-video')
def upload_video(uploaded_file: UploadFile = File(...)):
    file_location = f"/tmp/{uploaded_file.filename}"
    with open(file_location, "wb+") as file_object:
        file_object.write(uploaded_file.file.read())
        
    cap = cv2.VideoCapture(file_location)

    ret = {
        'filename': uploaded_file.filename,
        'size': uploaded_file.size,
        'width': cap.get(cv2.CAP_PROP_FRAME_WIDTH),
        'height': cap.get(cv2.CAP_PROP_FRAME_HEIGHT),
        'fps': cap.get(cv2.CAP_PROP_FPS),
        'frame_count': cap.get(cv2.CAP_PROP_FRAME_COUNT)
    }

    cap.release()

    return ret

@app.post('/predict')
def predict(uploaded_file: UploadFile = File(...)):
    # 1. 동영상 파일 임시 저장
    file_location = f"/tmp/{uploaded_file.filename}"
    with open(file_location, "wb+") as file_object:
        file_object.write(uploaded_file.file.read())
        
    cap = cv2.VideoCapture(file_location)
    fps = cap.get(cv2.CAP_PROP_FPS)
    
    # 2. 객체 추적 모델
    args = parse_opt([
        '--source', file_location,
        '--classes', '0',
        ])
    results = run(args)
    
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
        if len(data) == 0: continue
        frame_ids = np.full((data.shape[0], 1), i)
        t = np.hstack((frame_ids, data[:,:-1]))
        df = pd.concat([df, pd.DataFrame(t, columns=df.columns).astype(dtypes)], ignore_index=True)    
    

    # 3. 보행자 속성 인식
    track_id_list = []
    frame_enter_list = []
    frame_leave_list = []
    frame_stayed_list = []
    second_stayed_list = []
    age_list = []
    gender_list = []

    for track_id in df.track_id.unique():
        df_track = df.query(f'track_id == {track_id}')

        frame_stayed = df_track.frame_id.max() - df_track.frame_id.min()

        if frame_stayed / fps < 0.5:
            continue

        track_id_list.append(int(track_id))
        frame_enter_list.append(int(df_track.frame_id.min()))
        frame_leave_list.append(int(df_track.frame_id.max()))
        frame_stayed_list.append(int(frame_stayed))
        second_stayed_list.append(frame_stayed / fps)

        ages = defaultdict(int)
        genders = defaultdict(int)

        for i, row in df_track.iterrows():
            frame_id, left, top, right, bottom, track_id, _ = row
            top = round(top)
            left = round(left)
            right = round(right)
            bottom = round(bottom)
            cap.set(cv2.CAP_PROP_POS_FRAMES, frame_id)
            success, frame = cap.read()

            assert success

            src = frame[top:bottom, left:right]
            h, w, _ = src.shape
            if w < 10 or h < 10: # 탐지된 이미지가 너무 작은 경우 추론 X
                continue

            src = transforms(src)
            src = src.unsqueeze(0)
            with torch.no_grad():
                out = model.forward(src)
            age_idx = out[0,:4].argmax()
            ages[['young', 'teenager', 'adult', 'old'][age_idx]] += 1

            gender = 'male' if out[0, 12] < 0.5 else 'female'
            genders[gender] += 1
            
        age_list.append(max(ages.items(), key=lambda x:x[1])[0])
        gender_list.append(max(genders.items(), key=lambda x:x[1])[0])


    df_track = pd.DataFrame({
        'track_id': track_id_list,
        'frame_enter': frame_enter_list,
        'frame_leave': frame_leave_list,
        'frame_stayed': frame_stayed_list,
        'second_stayed': second_stayed_list,
        'age': age_list,
        'gender': gender_list,
    })

    ret = []
    for i, row in df_track.iterrows():
        track_id, frame_enter, frame_leave, frame_stayed, second_stayed, age, gender = row
        ret.append({
            'id': track_id,
            'frame_enter': frame_enter,
            'frame_leave': frame_leave,
            'frame_stayed': frame_stayed,
            'second_stayed': second_stayed,
            'age': age,
            'gender': gender,
        })
        
    return ret
    
    
    
    
    # 4. 머리 자세
    return {
        "info": f"file '{uploaded_file.filename}' saved at '{file_location}'"
    }


if __name__ == '__main__':
    import uvicorn
    uvicorn.run('main:app', host='*', port=8000)