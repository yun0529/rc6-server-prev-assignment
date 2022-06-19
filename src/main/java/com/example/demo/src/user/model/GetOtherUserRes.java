package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOtherUserRes {
    private int userIdx;
    private String userNickname;
    private String userImageUrl;
    private String userBackgroundImageUrl;
    private int subscribeCount;
    private int videoCount;
    private String userComment;
    private String createdAt;
    private String subscribe;
    private String alarmSet;
    private String totalViews;

    public GetOtherUserRes(int userIdx, String userNickname, String userImageUrl, String userBackgroundImageUrl, int subscribeCount,
                           int videoCount, String userComment, String createdAt, String totalViews) {
        this.userIdx = userIdx;
        this.userNickname = userNickname;
        this.userImageUrl = userImageUrl;
        this.userBackgroundImageUrl = userBackgroundImageUrl;
        this.subscribeCount = subscribeCount;
        this.videoCount = videoCount;
        this.userComment = userComment;
        this.createdAt = createdAt;
        this.totalViews = totalViews;
    }
}
