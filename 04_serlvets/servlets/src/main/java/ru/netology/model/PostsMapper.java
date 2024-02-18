package ru.netology.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostsMapper {
        default List<Post> mapListModelToListPost(List <PostModel> listPostsModel){
                final PostMapper mapperForOne
                        = Mappers.getMapper(PostMapper.class);
                return listPostsModel.stream().
                        filter(PostModel::isActive).
                        map(mapperForOne::modelToPost).
                        collect(Collectors.toList());
        }
}
