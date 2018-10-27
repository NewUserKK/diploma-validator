package com.wa285.validator.servlet;

import com.wa285.validator.parser.Parser;
import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.Location;
import com.wa285.validator.parser.errors.critical.DocumentFormatCriticalError;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class StaticServlet extends HttpServlet {

    private static int number = 1;

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

        // Создаём класс фабрику
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Максимальный буфера данных в байтах,
        // при его привышении данные начнут записываться на диск во временную директорию
        // устанавливаем один мегабайт
        factory.setSizeThreshold(1024 * 1024);

        // устанавливаем временную директорию
        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        //Создаём сам загрузчик
        ServletFileUpload upload = new ServletFileUpload(factory);

        //максимальный размер данных который разрешено загружать в байтах
        upload.setSizeMax(1024 * 1024 * 100);

        try {
            FileItem item = upload.parseRequest(request).get(0);

            File uploadedFile = processUploadedFile(item);
            List<Error> errors = findErrors(uploadedFile);
            BufferedWriter writeHtml = new BufferedWriter(new FileWriter(getServletContext().getRealPath("/static/textErrors.html")));

            var begin_end = new BufferedReader(new InputStreamReader(new FileInputStream(new File(getServletContext().getRealPath("/static/index.html"))))).lines().collect(Collectors.joining("")).split("<!--Docx_to-->");
            writeHtml.write(begin_end[0]);
            for (Error error : errors) {
                writeHtml.write(error.toString() + "<br>");
            }
            writeHtml.write(begin_end[1]);
            writeHtml.close();

            OutputStream outputStream = response.getOutputStream();
            File file = new File(getServletContext().getRealPath("/static/textErrors.html"));
            Files.copy(file.toPath(), outputStream);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private List<Error> findErrors(File file) throws IOException {
        return new Parser(file).findErrors();
    }

    private File processUploadedFile(FileItem item) throws Exception {
        File uploadedFile = null;
        //выбираем файлу имя пока не найдём свободное
        do {
            String path = getServletContext().getRealPath("/upload/" + number + "_" + item.getName());
            number++;
            uploadedFile = new File(path);
        } while (uploadedFile.exists());

        //создаём файл
        //записываем в него данные
        item.write(uploadedFile);
        return uploadedFile;
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
