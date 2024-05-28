from kafka import KafkaProducer
from json import dumps
import time
import datetime

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


def kafkaSend(producer, cameraId, interestFrameCnt, passedFrameCnt, arriveTime, leaveTime, staringframeData, male, age, fps):
    data = {'cameraId' : cameraId, 'startAt' : arriveTime, 'leaveAt' : leaveTime, 
            'passedFrameCnt' : passedFrameCnt, 'interestFrameCnt' : interestFrameCnt, "staringData" : staringframeData,
             "male": male, "age" : age, "fps" : fps}
    producer.send('drm-face-topic', value=data)
    # producer.poll() 
    
    

if __name__ == '__main__':
    producer = KafkaProducer(
    acks=0, 
    compression_type='gzip', 
    # bootstrap_servers=['43.203.218.109:29092'],
    bootstrap_servers=['localhost:29092'],
    value_serializer=lambda x:dumps(x).encode('utf-8') 
    )
    exampleTime =  time.localtime(time.time())
    printTime = datetime.datetime.now()
    
    print("KAFKA TEST START")
    print("TEST TIME : ", printTime.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3])
    for i in range(1000):
        # 아래의 테스트 전송 10개를 1 suite 로 지정
        if(i % 50 == 0):
            print("TEST PROCESSING : ", i)
        kafkaSend(producer, 2, 0, 5, exampleTime, exampleTime, [0,0,0,0,0], 0, 32, 10)
        kafkaSend(producer, 2, 9, 18, exampleTime, exampleTime, [0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1], 0, 18, 10)
        kafkaSend(producer, 2, 1, 5, exampleTime, exampleTime, [0,0,0,0,1], 1, 45, 5)
        
        kafkaSend(producer, 6, 9, 18, exampleTime, exampleTime, [0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1], 1, 80, 10)
        kafkaSend(producer, 6, 0, 5, exampleTime, exampleTime, [0,0,0,0,0], 0, 25, 10)
        kafkaSend(producer, 6, 6, 14, exampleTime, exampleTime, [0,0,0,1,1,1,0, 0,0,0,1,1,1,0], 0, 23, 10)
        kafkaSend(producer, 6, 9, 18, exampleTime, exampleTime, [0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1], 0, 22, 5)

        kafkaSend(producer, 3, 9, 18, exampleTime, exampleTime, [0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1], 1, 15, 10)
        kafkaSend(producer, 3, 6, 14, exampleTime, exampleTime, [0,0,0,1,1,1,0, 0,0,0,1,1,1,0], 1, 20, 10)
        kafkaSend(producer, 3, 9, 18, exampleTime, exampleTime, [0,0,0,1,1,1,0,0,0,1,1,1,0,0,0,1,1,1], 0, 27, 5)
    producer.flush()
    printEndTime = datetime.datetime.now()
    print("TEST TIME : ", printTime.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3])
    print("TEST END TIME : ", printEndTime.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3])
    print("TEST SEND DONE")
