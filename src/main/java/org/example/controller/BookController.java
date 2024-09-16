package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.BookDto;
import org.example.service.BookService;
import org.example.service.impl.BookServiceImpl;

import java.io.IOException;

@WebServlet(value = "/book/*")
public class BookController extends HttpServlet {

    private transient BookService service = BookServiceImpl.getInstance();
    private final ObjectMapper objectMapper;

    private static final String BAD_REQUEST = "Bad request";
    private static final String NOT_FOUND = "Not found";

    public BookController() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String response;
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                response = getAll();
            } else {
                Integer id = Integer.valueOf(pathInfo.split("/")[1]);
                response = getById(id);
            }
        } catch (Exception e) {
            response = BAD_REQUEST;
        }

        if (response.equals(BAD_REQUEST)) resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else if (response.equals(NOT_FOUND)) resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        else resp.setStatus(HttpServletResponse.SC_OK);

        resp.getWriter().write(response);
    }

    private String getAll() throws IOException {
        return objectMapper.writeValueAsString(service.findAll());
    }

    private String getById(Integer id) throws IOException {
        if (id == null || id < 1) return BAD_REQUEST;

        BookDto book = service.findById(id);
        if (book == null) return NOT_FOUND;

        return objectMapper.writeValueAsString(book);
    }
}
