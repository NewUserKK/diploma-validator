package com.wa285.validator.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        if (request.getRequestURI().length() == 0 || request.getRequestURI().equals("/")) {
            File file = new File(getServletContext().getRealPath("/static/index.html"));
            Files.copy(file.toPath(), outputStream);
        } else {
            String[] allUri = request.getRequestURI().split("\\+");

            response.setContentType(getContentTypeFromName(allUri[0]));
            for (String uri : allUri) {
                if (uri.contains("/") && !uri.startsWith("/")) {
                    uri = "/" + uri;
                }
                File file = new File(System.getProperty("user.dir") + "/src/main/webapp/static" + uri);
                if (file.isFile()) {
                    Files.copy(file.toPath(), outputStream);
                } else {
                    file = new File(getServletContext().getRealPath("/static" + uri));
                    if (file.isFile()) {
                        Files.copy(file.toPath(), outputStream);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            }
        }
        outputStream.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
