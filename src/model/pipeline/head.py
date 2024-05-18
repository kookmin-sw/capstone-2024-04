import os
import sys

# TODO: 저장소 경로 수정
sys.path.append('/root/6DRepNet/sixdrepnet')

import torch
from torchvision import transforms
from PIL import Image
import numpy as np
import cv2

from face_detection import RetinaFace
from model import SixDRepNet
import utils

transformations = transforms.Compose([transforms.Resize(224),
                                      transforms.CenterCrop(224),
                                      transforms.ToTensor(),
                                      transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])])

class Head:
    def __init__(self):
        self.model = SixDRepNet(backbone_name='RepVGG-B1g2',
                               backbone_file='',
                               deploy=True,
                               pretrained=False)
        self.detector = RetinaFace(gpu_id=0)
        # TODO: 가중치 경로 수정
        snapshot_path = '/root/6DRepNet/6DRepNet_300W_LP_AFLW2000.pth'
        saved_state_dict = torch.load(os.path.join(snapshot_path), map_location='cpu')
        if 'model_state_dict' in saved_state_dict:
            self.model.load_state_dict(saved_state_dict['model_state_dict'])
        else:
            self.model.load_state_dict(saved_state_dict)
        self.model.to('cuda')
        self.model.eval()

    def predict(self, image):
        """
        Args:
            image: 한 사람의 외형 이미지
        """
        faces = self.detector(image)
        results = []
        for box, landmarks, score in faces:
                # Print the location of each face in this image
                if score < .95:
                    continue
                x_min = int(box[0])
                y_min = int(box[1])
                x_max = int(box[2])
                y_max = int(box[3])
                bbox_width = abs(x_max - x_min)
                bbox_height = abs(y_max - y_min)

                x_min = max(0, x_min-int(0.2*bbox_height))
                y_min = max(0, y_min-int(0.2*bbox_width))
                x_max = x_max+int(0.2*bbox_height)
                y_max = y_max+int(0.2*bbox_width)

                img = image[y_min:y_max, x_min:x_max]
                img = Image.fromarray(img)
                img = img.convert('RGB')
                img = transformations(img)

                img = torch.Tensor(img[None, :]).to('cuda')

                R_pred = self.model(img)

                euler = utils.compute_euler_angles_from_rotation_matrices(
                    R_pred)*180/np.pi
                p_pred_deg = euler[:, 0].cpu().item()
                y_pred_deg = euler[:, 1].cpu().item()
                r_pred_deg = euler[:, 2].cpu().item()
                results.append((p_pred_deg, y_pred_deg, r_pred_deg))
            
                utils.plot_pose_cube(image,  y_pred_deg, p_pred_deg, r_pred_deg, x_min + int(.5*(
                    x_max-x_min)), y_min + int(.5*(y_max-y_min)), size=bbox_width)
    
        if len(results) == 0:
            return 0
        else:
            return results[0][0]

if __name__ == '__main__':
    head = Head()
    image = cv2.imread('lena.jpg')
    results = head.predict(image)
    print(results)