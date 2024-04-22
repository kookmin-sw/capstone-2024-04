import os
import sys

sys.path.append(os.path.abspath(os.path.join(__file__, "../../../pipeline/")))

import streamlit as st
from PIL import Image
import numpy as np

from pipeline import Pipeline

st.write('# 객체 검출 데모 페이지')
uploaded_file = st.file_uploader("이미지 파일 업로드")

if uploaded_file is not None:
    image = Image.open(uploaded_file)
    image = np.array(image)

    pipeline = Pipeline()
    results = pipeline.detect(image)
    assert len(results) == 1

    for result in results:
        plotted_iamge = result.plot()

    st.image(plotted_iamge)