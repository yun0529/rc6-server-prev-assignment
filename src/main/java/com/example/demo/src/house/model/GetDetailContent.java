package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDetailContent {
    private int roomContentIdx;
    private String contentName;
    private String detailType;
}
