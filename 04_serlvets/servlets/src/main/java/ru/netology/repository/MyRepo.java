package ru.netology.repository;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;
import ru.netology.model.Post;
import ru.netology.model.PostMapper;
import ru.netology.model.PostModel;
import ru.netology.model.PostsMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MyRepo implements PostRepository{

    private final PostMapper mapperForOne
            = Mappers.getMapper(PostMapper.class);
    private final PostsMapper mapperForList
            = Mappers.getMapper(PostsMapper.class);

    private final Map<Long, PostModel> postsMap;
    private final AtomicLong baseID = new AtomicLong(1);

    public MyRepo() {
        postsMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<Post> all() {
        return mapperForList.mapListModelToListPost(new ArrayList<>(postsMap.values()));
    }

    @Override
    public Optional<Post> getById(long id) {
        if (postsMap.containsKey(id) && postsMap.get(id).isActive()) {
            return Optional.ofNullable(mapperForOne.modelToPost(postsMap.get(id)));
        }
        return Optional.empty();
    }

    @Override
    public Post save(Post post) {
        PostModel postModel = mapperForOne.postToModel(post);
        postModel.setActive(true);
        if (post.getId() == 0) {
            while (postsMap.containsKey(baseID.get())) {
                baseID.getAndIncrement();
            }
            postModel.setId(baseID.get());
            postsMap.put(baseID.get(), postModel);
        } else {
            postsMap.put(post.getId(), postModel);
        }
        return mapperForOne.modelToPost(postModel);
    }

    @Override
    public void removeById(long id) {
        if (postsMap.containsKey(id)) {
            postsMap.get(id).setActive(false);
            if (id < baseID.get()) {
                baseID.set(id);
            }
        }
    }
}
