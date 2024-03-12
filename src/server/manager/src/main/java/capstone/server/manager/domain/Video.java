package capstone.server.manager.domain;


import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;


@Entity
public class Video {
    @Id
    int video_id;
    String video_link;
    String title;
    LocalTime video_length;
    @OneToMany(mappedBy = "video")
    private List<DetectedData> detectedDataList;
    @ManyToOne
    @JoinColumn
    private Video_enrollment video_enrollment;
}
