package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSubscribeUserRes {
    private int subscribeUserIdx;
    private String subscribeUserNickname;
    private String subscribeUserImageUrl;
    private int subscribeCount;
    private int videoCount;
    private String subscribe;
    private String alarmSet;
}
