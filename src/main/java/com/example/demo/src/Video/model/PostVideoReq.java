package com.example.demo.src.Video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoReq {
    private int userIdx;
    private String videoThumbnailImageUrl;
    private String videoUrl;
    private String videoTitle;
    private String videoTime;
    private String videoComment;
    private String videoLocation;
    private String videoListIdx;
    private String videoChild;
    private String videoAge;
    private String videoTag;
    private String videoChatAllow;
    private String videoCategory;
}
