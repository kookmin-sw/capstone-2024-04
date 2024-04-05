from kafka import KafkaProducer
from json import dumps
import time

# 사용 라이브러리 -> pip install kafka-python 
# 서버 주소는 클라우드 배포 이후 변경 예정

# Parameter 값 설명
# cameraId => 임의로 보내는 카메라 Id 값 (하나의 모델이라고 가정시 같은 Integer 값으로 보내면 된다.)
# interestPeopleCnt => 등장한 인물의 관심 있는 것으로 추정되는 프레임의 개수 (int)
# arriveTime ->  인물의 등장 시간 - time.localtime() 형식 (문제 있을 시 변경 요청 해주셔도 됩니다.)
# leaveTime -> 인물의 퇴장 시간 - time.localtime()
# staringframeData -> 인물 등장 관련 0,1 표현 데이터 리스트 

def kafkaSend(cameraId, interestPeopleCnt, passedPeopleCnt, arriveTime, leaveTime, staringframeData):
    producer = KafkaProducer(
    acks=0, 
    compression_type='gzip', 
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda x:dumps(x).encode('utf-8') 
    )
    
    data = {'cameraId' : cameraId, 'startAt' : arriveTime, 'leaveAt' : leaveTime, 'passedPeopleCnt' : passedPeopleCnt, 'interestPeopleCnt' : interestPeopleCnt, "staringData" : staringframeData}
    producer.send('drm-face-topic', value=data)
    producer.flush() 
    
    

if __name__ == '__main__':
    exampleTime =  time.localtime(time.time()) 
    kafkaSend(0, 12, 30, exampleTime, exampleTime, [0,0,0,1,1,1,0])
