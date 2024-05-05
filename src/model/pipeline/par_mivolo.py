import sys
# TODO : 환경변수로 수정
sys.path.append('/home/kh/capstone/PAR/mivolo')

import os

import cv2
import torch
import torchvision.transforms.functional as F
from timm.data import IMAGENET_DEFAULT_MEAN, IMAGENET_DEFAULT_STD
mean=IMAGENET_DEFAULT_MEAN
std=IMAGENET_DEFAULT_STD

from mivolo.predictor import Predictor
from mivolo.model.mi_volo import MiVOLO
from mivolo.data.misc import prepare_classification_images

class PAR:
    def __init__(self):
        self.model = MiVOLO(
            # TODO : 환경변수로 수정
            '/home/kh/capstone/PAR/mivolo/models/mivolo_imbd.pth.tar',
            'cuda:0',
            half=True,
            use_persons=True,
            disable_faces=True,
            verbose=True,
        )

    @torch.no_grad()
    def predict(self, image):
        """
        Args:
            image: 이미지 경로 혹은 ndarray
                한 사람의 외형만 잘린 형태의 이미지여야 함
        """
        if isinstance(image, (str, os.PathLike)):
            image = cv2.imread(image)

        body = prepare_classification_images([image])

        face = torch.zeros((3, 224, 224), dtype=torch.float32)
        face = F.normalize(face, mean=mean, std=std)
        face = face.unsqueeze(0)

        x = torch.cat((face, body), dim=1)
        x = x.cuda()

        out = self.model.inference(x)
        gender_output = out[:, :2].softmax(-1)
        gender_probs, gender_indx = gender_output.topk(1)
        age = out[:,2][0].item()
        age = age * (95 - 1) + 48
        age = round(age, 2)
        return {
            'age': age,
            'gender': ['male', 'female'][gender_indx]
        }

if __name__ == '__main__':
    import os
    assert os.path.exists('lena.jpg')
    
    par = PAR()
    result = par.predict('lena.jpg')
    print(result)

    im = cv2.imread('lena.jpg')
    result = par.predict(im)
    print(result)