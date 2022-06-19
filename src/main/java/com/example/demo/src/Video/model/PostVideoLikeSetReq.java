package com.example.demo.src.Video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostVideoLikeSetReq {
    private int userIdx;
    private int videoIdx;
}
