package com.example.demo.src.Video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModifyVideos {
    private int videoIdx;
    private int userIdx;
    private String videoTitle;
    private String videoComment;
    private String status;
    private String videoChild;
    private String videoAge;
    private String videoLocation;
    private int videoListIdx;
    private String videoTag;
}