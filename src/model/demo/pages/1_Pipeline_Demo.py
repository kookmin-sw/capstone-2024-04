import os
import sys

sys.path.append(os.path.abspath(os.path.join(__file__, "../../../pipeline/")))
import streamlit as st

from pipeline import Pipeline

from kafka import KafkaProducer
from json import dumps
import time

KAFKA_SERVER = os.getenv('KAFKA_SERVER')

def kafkaSend(cameraId, interestPeopleCnt, passedPeopleCnt, arriveTime, leaveTime, staringframeData, male, age, fps):
    producer = KafkaProducer(
    acks=0,
    compression_type='gzip',
    bootstrap_servers=[KAFKA_SERVER],
    value_serializer=lambda x:dumps(x).encode('utf-8')
    )

    data = {'cameraId' : cameraId, 'startAt' : arriveTime, 'leaveAt' : leaveTime,
            'passedPeopleCnt' : passedPeopleCnt, 'interestPeopleCnt' : interestPeopleCnt,
            "   staringData" : staringframeData,
            "male": male, "age" : age, "fps" : fps}
    producer.send('drm-face-topic', value=data)
    producer.flush()


st.write('# 전체 파이프라인 데모 페이지')
st.write('동영상 파일을 업로드하면 전체 파이프라인에서 동영상을 처리하고 Kafka 서버에 추론 결과를 전송합니다.')
uploaded_file = st.file_uploader("동영상 파일 업로드")

if uploaded_file is not None:
    save_path = os.path.join('/tmp', uploaded_file.name)
    with open(save_path, 'wb') as f:
        f.write(uploaded_file.getbuffer())

    pipeline = Pipeline()
    info_list = pipeline.run(save_path, visualize=True)

    st.write(info_list)

    for info in info_list:
        t = time.localtime(time.time())
        kafkaSend(3,
                sum(info.staring_list),
                len(info.staring_list),
                t,
                t,
                info.staring_list,
                1 if info.gender == 'male' else 0,
                20,
                30
                )
        st.write(f'SEND ID: {info.id}')