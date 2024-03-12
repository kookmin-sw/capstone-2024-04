package capstone.server.manager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Video_enrollment {
    @Id
    private int video_enrollment_id;
//    String title;
//    String content;
    @OneToMany(mappedBy = "video_enrollment")
    private List<Video> videoList;
}
