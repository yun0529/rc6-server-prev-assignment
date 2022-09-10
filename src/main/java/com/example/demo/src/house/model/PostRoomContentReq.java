package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostRoomContentReq {
    private int roomIdx;
    private String contentName;
    private String contentType;
    private int contentCount;
    private String contentComment;
}
