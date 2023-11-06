package com.example.controllerexample.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "title")
    String title;

    @Size(min = 1, max = 180, message = "desc is should not be greater 1 and less than 180")
    @Column(name = "post_desc")
    String desc;

    public Post() {
    }

    public Post(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public Post(long id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
