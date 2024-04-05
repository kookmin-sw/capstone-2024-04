import os

import streamlit as st
import cv2

LOOK_DIR = os.getenv('LOOK_DIR')
assert LOOK_DIR is not None

st.write('# LOOK(아이 컨택) 모델 데모 페이지')
uploaded_file = st.file_uploader("이미지 파일 업로드")

if uploaded_file is not None:
    save_path = os.path.join('/tmp', uploaded_file.name)
    with open(save_path, 'wb') as f:
        f.write(uploaded_file.getbuffer())
        
    os.system('cd {} && python predict.py --images {} --device cpu --disable-cuda -o /tmp'.format(LOOK_DIR, save_path))
    
    name, ext = os.path.splitext(uploaded_file.name)
    result_path = os.path.join('/tmp', name + '.predictions' + ext)
    im_result = cv2.imread(result_path)
    st.image(im_result)