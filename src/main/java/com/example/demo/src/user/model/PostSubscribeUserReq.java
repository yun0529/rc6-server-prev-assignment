package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSubscribeUserReq {
    private int userIdx;
    private int subscribeUserIdx;
}
