package com.example.controllerexample.post;


import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "postList")
public record PostResponse(long id, String content) {}

