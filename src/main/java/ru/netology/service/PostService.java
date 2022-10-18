package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id, HttpServletResponse response) {
        try {
            return repository.getById(id).orElseThrow(NotFoundException::new);
        } catch (NotFoundException e) {
            setStatusResponseNotFound(response);
            return null;
        }
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void removeById(long id, HttpServletResponse response) {
        if (repository.hasId(id))
            repository.removeById(id);
        else
            setStatusResponseNotFound(response);
    }

    private void setStatusResponseNotFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}

