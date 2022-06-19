package com.example.demo.src.Video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetVideoRes {
    private int videoIdx;
    private int userIdx;
    private String videoThumbnailImageUrl;
    private String videoUrl;
    private String videoTitle;
    private String userNickname;
    private int viewCount;
    private String createdAt;
}
