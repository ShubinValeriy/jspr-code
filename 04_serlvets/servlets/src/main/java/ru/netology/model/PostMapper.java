package ru.netology.model;


import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {
    Post modelToPost(PostModel postModel);
    PostModel postToModel(Post post);
}
