from .tracker import Tracker

class Pipeline:
    def __init__(self):
        self.tracker = Tracker()
        
    def detect(self, image_path):
        results = self.tracker.detect(image_path)
        return results
    
    def track(self, video_path):
        results = self.tracker.track(video_path)
        return results
        
    def par(self, inage_array):
        ...
        
    def head_pose(self, image_array):
        ...

    def gaze(self, image_array):
        ...
    
    def run(self, video_path):
        ...