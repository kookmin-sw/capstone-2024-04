package capstone.server.manager.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetectedData {
    @Id
    private int detectedData_id;
    private int duration; // Duration of the advertisement segment in seconds
    private int passedPeopleCnt; // Number of people who passed during the segment
    private int interestedPeopleCnt; // Number of people who showed interest during the segment
    @ManyToOne
    @JoinColumn
    private Video video;
}

