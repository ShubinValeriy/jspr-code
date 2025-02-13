package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.model.MethodHTTP;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private final String POSTS_PATH = "/api/posts";
    private final String ID_POST_PATH = "/api/posts/\\d+";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (MethodHTTP.valueOf(method).equals(MethodHTTP.GET) && path.equals(POSTS_PATH)) {
                controller.all(resp);
                return;
            }
            if (MethodHTTP.valueOf(method).equals(MethodHTTP.GET) &&
                    path.matches(ID_POST_PATH)) {
                // easy way
                final var id = getIDFromPath(path);
                controller.getById(id, resp);
                return;
            }
            if (MethodHTTP.valueOf(method).equals(MethodHTTP.POST) && path.equals(POSTS_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (MethodHTTP.valueOf(method).equals(MethodHTTP.DELETE)
                    && path.matches(ID_POST_PATH)) {
                // easy way
                final var id = getIDFromPath(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getIDFromPath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

