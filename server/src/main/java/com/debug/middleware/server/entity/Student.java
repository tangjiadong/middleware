package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Tang on 2020/7/15
 */
@Data
@ToString
public class Student {
    private String id;
    private String userName;
    private String name;

    public Student() {
    }

    public Student(String id, String userName, String name) {
        this.id = id;
        this.userName = userName;
        this.name = name;
    }
}
