import sys
import logging

import cv2

from ptgaze.main import (parse_args, load_mode_config, OmegaConf,
                         expanduser_all, generate_dummy_camera_params,
                         download_dlib_pretrained_model, download_mpiigaze_model,
                         download_mpiifacegaze_model, logger,
                         download_ethxgaze_model, check_path_all, Demo)

class Gaze:
    def __init__(self):
        self.demo = None
        
    def get_demo(self, image_path):
        argv = sys.argv
        sys.argv = [
            'ptgaze', '--no-screen', '--mode', 'eth-xgaze', '--image', image_path
        ]
        args = parse_args()
        sys.argv = argv
        
        if args.debug:
            logging.getLogger('ptgaze').setLevel(logging.DEBUG)

        if args.config:
            config = OmegaConf.load(args.config)
        elif args.mode:
            config = load_mode_config(args)
        else:
            raise ValueError(
                'You need to specify one of \'--mode\' or \'--config\'.')
        expanduser_all(config)
        if config.gaze_estimator.use_dummy_camera_params:
            generate_dummy_camera_params(config)

            OmegaConf.set_readonly(config, True)
            logger.info(OmegaConf.to_yaml(config))

            if config.face_detector.mode == 'dlib':
                download_dlib_pretrained_model()
            if args.mode:
                if config.mode == 'MPIIGaze':
                    download_mpiigaze_model()
                elif config.mode == 'MPIIFaceGaze':
                    download_mpiifacegaze_model()
                elif config.mode == 'ETH-XGaze':
                    download_ethxgaze_model()

            check_path_all(config)
        
        demo = Demo(config)
        self.demo = demo
        return demo
        
        
    def estimate(self, image_path):
        demo = self.get_demo(image_path)
        
        image = cv2.imread(demo.config.demo.image_path)

        undistorted = cv2.undistort(
            image, demo.gaze_estimator.camera.camera_matrix,
            demo.gaze_estimator.camera.dist_coefficients)
        
        demo.visualizer.set_image(image.copy())
        faces = demo.gaze_estimator.detect_faces(undistorted)
        for face in faces:
            demo.gaze_estimator.estimate_gaze(undistorted, face)
            demo._draw_face_bbox(face)
            demo._draw_head_pose(face)
            demo._draw_landmarks(face)
            demo._draw_gaze_vector(face)
            
        return faces
    

if __name__ == '__main__':
    import os
    assert os.path.exists('lena.jpg')
    
    gaze = Gaze()
    faces = gaze.estimate('lena.jpg')
    face = faces[0]
    x, y, z = face.head_position
    gx, gy, gz = face.gaze_vector

    # z + gz * t = 0
    t = -z / gz
    screen_x = x + gx * t
    screen_y = y + gy * t
    screen_z = z + gz * t
    print(f'Head Position : {x:.4f}, {y:.4f}, {z:.4f}')
    print(f'Gaze Vector   : {gx:.4f}, {gy:.4f}, {gz:.4f}')
    print(f'Point of Gaze : {screen_x:.4f}, {screen_y:.4f}, {screen_z:.4f}')