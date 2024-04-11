import os
import warnings

import cv2
import numpy as np
import torch
import torch.nn as nn


class LookingModel(nn.Module):
    """
    Class definition for the Looking Model.
    """
    def __init__(self, input_size, p_dropout=0.2, output_size=1, linear_size=256, num_stage=3):
        """[summary]

        Args:
            input_size (int): Input size for the model. If the whole pose needs to be used, the value should be 51.
            p_dropout (float, optional): Dropout rate in the linear blocks. Defaults to 0.2.
            output_size (int, optional): Output number of nodes. Defaults to 1.
            linear_size (int, optional): Size of the fully connected layers in the Linear blocks. Defaults to 256.
            num_stage (int, optional): Number of stages to use in the Linear Block. Defaults to 3.
            bce (bool, optional): Make use of the BCE Loss. Defaults to False.
        """
        super(LookingModel, self).__init__()

        self.input_size = input_size
        self.output_size = output_size
        self.linear_size = linear_size
        self.p_dropout = p_dropout
        self.num_stage = num_stage
        self.relu = nn.ReLU(inplace=True)
        self.dropout = nn.Dropout(self.p_dropout)

        # process input to linear size
        self.w1 = nn.Linear(self.input_size, self.linear_size)
        self.batch_norm1 = nn.BatchNorm1d(self.linear_size)

        self.linear_stages = []
        for _ in range(num_stage):
            self.linear_stages.append(Linear(self.linear_size, self.p_dropout))
        self.linear_stages = nn.ModuleList(self.linear_stages)

        # post processing
        self.w2 = nn.Linear(self.linear_size, self.output_size)
        self.sigmoid = nn.Sigmoid()

    def forward_first_stage(self, x):
        # pre-processing
        y = self.w1(x)
        y = self.batch_norm1(y)
        y = self.relu(y)
        y = self.dropout(y)
        return y
    
    def forward_second_stage(self, y):
        for i in range(self.num_stage):
            y = self.linear_stages[i](y)
        #y = self.binarize(y)
        y = self.w2(y)
        #y = self.binarize(y)
        y = self.sigmoid(y)
        return y

    def forward(self, x):
        # pre-processing
        y = self.w1(x)
        y = self.batch_norm1(y)
        y = self.relu(y)
        y = self.dropout(y)
        # linear layers
        for i in range(self.num_stage):
            y = self.linear_stages[i](y)
        #y = self.binarize(y)
        y = self.w2(y)
        #y = self.binarize(y)
        y = self.sigmoid(y)
        return y


class Linear(nn.Module):
    """
    Class definition of the Linear block
    """
    def __init__(self, linear_size=256, p_dropout=0.2):
        """
        Args:
            linear_size (int, optional): Size of the FC layers inside the block. Defaults to 256.
            p_dropout (float, optional): Dropout rate. Defaults to 0.2.
        """
        super(Linear, self).__init__()
        ###

        self.linear_size = linear_size
        self.p_dropout = p_dropout

        ###

        self.relu = nn.ReLU(True)
        self.dropout = nn.Dropout(self.p_dropout)

        ###
        self.l1 = nn.Linear(self.linear_size, self.linear_size)
        self.bn1 = nn.BatchNorm1d(self.linear_size)

        self.l2 = nn.Linear(self.linear_size, self.linear_size)
        self.bn2 = nn.BatchNorm1d(self.linear_size)
        
    def forward(self, x):
        # stage I

        y = self.l1(x)
        y = self.bn1(y)
        y = self.relu(y)
        y = self.dropout(y)

        # stage II

        
        y = self.l2(y)
        y = self.bn2(y)
        y = self.relu(y)
        y = self.dropout(y)

        # concatenation

        out = x+y

        return out
    
def normalize_by_image_(X, Y, image_size):
    """
        Normalize the image according to the paper.
        Args:
            - X: array of X positions of the keypoints
            - Y: array of Y positions of the keypoints
            - Image: Image array
        Returns:
            returns the normalized arrays
    """
    
    image_width, image_height = image_size
    
    center_p = (int((X[11] + X[12]) / 2), int((Y[11] + Y[12]) / 2))
    X_new = np.array(X)/image_width
    Y_new = np.array(Y)-center_p[1]


    width = abs(np.max(X) - np.min(X))
    height = abs(np.max(Y) - np.min(Y))

    X_new = X_new + ((np.array(X)-center_p[0])/width)
    Y_new /= height
    
    return X_new, Y_new


class LOOK:
    LOOK_WEIGHT_PATH = os.getenv('LOOK_WEIGHT_PATH')
    def __init__(self):
        self.model = LookingModel(51)
        if LOOK.LOOK_WEIGHT_PATH is not None:
            self.model.load_state_dict(torch.load(LOOK.LOOK_WEIGHT_PATH))
        else:
            warnings.warn('LOOK model weight is not given.')
        self.model.eval()
        ...
        
    @torch.no_grad()
    def predict(self, keypoints, image_size):
        """
        Args:
            keypoints: (-1, 17, 3) 형태의 텐서 또는 ndarray
            image_size: (height, width) 형태 튜플
        """
        final_keypoints = []
        for kp in keypoints:
            X = kp[:,0]
            Y = kp[:,1]
            X, Y = normalize_by_image_(X, Y, image_size)
            kps_final_normalized = np.array([X, Y, kp[:, 2]]).flatten().tolist()
            final_keypoints.append(kps_final_normalized)
        tensor_kps = torch.Tensor([final_keypoints])
        out = self.model(tensor_kps.squeeze(0)).detach().cpu().numpy().reshape(-1)
        return out


if __name__ == '__main__':
    from tracker import Tracker
    look = LOOK()
    tracker = Tracker()
    results = tracker.detect('lena.jpg')
    keypoints = results[0].keypoints.data.detach().cpu().numpy()
    h, w, c = results[0].orig_img.shape
    image_size = (h, w)
    out = look.predict(keypoints, image_size)
    print(out)