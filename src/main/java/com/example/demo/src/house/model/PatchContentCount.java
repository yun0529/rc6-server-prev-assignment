package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchContentCount {
    private int roomIdx;
    private String contentName;
    private int contentCount;
}
