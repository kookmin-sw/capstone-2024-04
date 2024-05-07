package com.drm.server.controller.dto.response;

import com.drm.server.domain.playlist.PlayList;
import lombok.Getter;
import lombok.Setter;

public class PlayListResponse {
    @Getter
    @Setter
    public static class TodayList{
        private Long playListId;
        private boolean broadCasting;
        private LocationResponse.LocationInfo location;
        private MediaResponse.MediaInfo media;


        public TodayList(PlayList playList) {
            this.playListId = playList.getPlayListId();
            this.broadCasting = playList.isPosting();
            this.location = new LocationResponse.LocationInfo(playList.getLocation());
            this.media = new MediaResponse.MediaInfo(playList.getMediaApplication().getMedia());
        }
    }


}
