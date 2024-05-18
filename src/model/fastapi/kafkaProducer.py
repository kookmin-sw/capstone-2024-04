from kafka import KafkaProducer
from json import dumps
import time

# 사용 라이브러리 -> pip install kafka-python 

# Parameter 값 설명

# cameraId => 임의로 보내는 카메라 Id 값 (하나의 모델이라고 가정시 같은 Integer 값으로 보내면 된다.) => 테스트시 1,2,3 으로 보내기 ! 

# interestFrameCnt => 등장한 인물의 관심 있는 것으로 추정되는 프레임의 개수 (int)

# passedFrameCnt => 등장한 인물의 관심 있는 것으로 추정되는 프레임의 개수 (int)

# arriveTime ->  인물의 등장 시간 - time.localtime() 형식 (문제 있을 시 변경 요청 해주셔도 됩니다.)

# leaveTime -> 인물의 퇴장 시간 - time.localtime()

# staringframeData -> 인물 등장하고 관심 표현 여부 0,1로 나타낸 리스트

# male -> 남성 : 1  / 여성 : 0

# age -> 추정 나이 - ex: 23 (integer)

# fps ->  frame per second - 1 로 통일

def kafkaSend(cameraId, interestFrameCnt, passedFrameCnt, arriveTime, leaveTime, staringframeData, male, age, fps):
    producer = KafkaProducer(
    acks=0, 
    compression_type='gzip', 
    # 로컬 서버
    # bootstrap_servers=['localhost:9092'],
    # 클라우드 카프카 서버 -> 테스트시 가능하면 해당 서버로 보내주세요.
    bootstrap_servers=['3.34.47.236:29092'],
    value_serializer=lambda x:dumps(x).encode('utf-8') 
    )
    
    data = {'cameraId' : cameraId, 'startAt' : arriveTime, 'leaveAt' : leaveTime, 
            'passedFrameCnt' : passedFrameCnt, 'interestFrameCnt' : interestFrameCnt, "staringData" : staringframeData,
             "male": male, "age" : age, "fps" : fps}
    producer.send('drm-face-topic', value=data)
    producer.flush() 
    
    

if __name__ == '__main__':
    exampleTime =  time.localtime(time.time())
    kafkaSend(1, 0, 5, exampleTime, exampleTime, [0,0,0,0,0], 0, 45, 1)
    # kafkaSend(1, 1, 5, exampleTime, exampleTime, [0,0,0,0,1], 1, 45, 1)
    # kafkaSend(2, 5, 22, exampleTime, exampleTime, [0,0,0,1,1,1,0], 0, 45, 1)
    # kafkaSend(3, 10, 25, exampleTime, exampleTime, [0,0,0,1,1,1,0], 0, 45, 1)
