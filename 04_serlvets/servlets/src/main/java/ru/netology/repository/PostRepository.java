package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final Map<Long, Post> postsMap;
    private final AtomicLong baseID = new AtomicLong(1);

    public PostRepository() {
        postsMap = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(postsMap.values());
    }

    public Optional<Post> getById(long id) {
        if (postsMap.containsKey(id)) {
            return Optional.ofNullable(postsMap.get(id));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            while (postsMap.containsKey(baseID.get())) {
                baseID.getAndIncrement();
            }
            post.setId(baseID.get());
            postsMap.put(baseID.get(), post);
        } else {
            postsMap.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        if (postsMap.containsKey(id)) {
            postsMap.remove(id);
            if (id < baseID.get()) {
                baseID.set(id);
            }
        }
    }
}
