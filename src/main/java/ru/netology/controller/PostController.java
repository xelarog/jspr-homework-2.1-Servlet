package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        // TODO: deserialize request & serialize response
        response.setContentType(APPLICATION_JSON);
        final var data = service.getById(id, response);
        if (data != null) {
            final var gson = new Gson();
            response.getWriter().print(gson.toJson(data));
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        if (data != null)
            response.getWriter().print(gson.toJson(data));
        else
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    public void removeById(long id, HttpServletResponse response) {
        // TODO: deserialize request & serialize response
        service.removeById(id, response);
    }
}
