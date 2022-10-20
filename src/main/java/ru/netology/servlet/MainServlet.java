package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

  private final String get = "GET";
  private final String post = "POST";
  private final String delete = "DELETE";

  private PostController controller;

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.netology");
    controller = (PostController) context.getBean("postController");
    final var service = context.getBean("postService");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      final var pathWithoutId = "/api/posts";
      final var pathWithId = "/api/posts/\\d+";

      // primitive routing
      if (method.equals(get) && path.equals(pathWithoutId)) {
        controller.all(resp);
        return;
      }

      if (method.equals(post) && path.equals(pathWithoutId)) {
        controller.save(req.getReader(), resp);
        return;
      }

      //routing with id
      if (path.matches(pathWithId)) {
        final var id = parseIdFromPath(path);
        if (method.equals(get)) {
          controller.getById(id, resp);
          return;
        } else if (method.equals(delete)) {
          controller.removeById(id, resp);
          return;
        }
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parseIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }

}

