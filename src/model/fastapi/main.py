import cv2

from fastapi import FastAPI, File, UploadFile

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


if __name__ == '__main__':
    import uvicorn
    uvicorn.run('main:app', host='*', port=8000)