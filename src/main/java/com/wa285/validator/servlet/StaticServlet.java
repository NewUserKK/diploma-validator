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
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class StaticServlet extends HttpServlet {

    private static int number = 1;
    private String begin = "<!DOCTYPE html>\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n" +
            "    <title>Пример создания поля загрузки файлов</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"text\" tabindex=\"-1\">";
    private String end = "</div>\n" +
            "    <div class=\"buttons\">\n" +
            "        <form method=\"post\" enctype=\"multipart/form-data\">\n" +
            "            <input name=\"user-file\" type=\"file\">\n" +
            "            <input type=\"submit\" value=\"Отправить\">\n" +
            "        </form>\n" +
            "        <!--<div class=\"FindDiff\">\n" +
            "            <input type=\"submit\" value=\"Find diff\">\n" +
            "        </div>-->\n" +
            "\n" +
            "        <div class=\"download\">\n" +
            "            <!--<form method=\"get\" action=\"img/comments_16x16.png\">-->\n" +
            "                <!--<button type=\"submit\">Скачать</button>-->\n" +
            "            <!--</form>-->\n" +
            "            <!--<a href=\"img/comments_16x16.png\" download>Скачать файл</a>-->\n" +
            "            <!--<input type=\"button\" onclick=\"location\" href=\"img/comments_16x16.png\" value=\"Скачать\">-->\n" +
            "            <button type=\"submit\" onclick=\"window.open('img/logo.png')\">Download!</button>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n";

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

    private Random random = new Random();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*//проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }*/

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
        //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт.
        upload.setSizeMax(1024 * 1024 * 100);

        try {
            List items = upload.parseRequest(request);

            for (Object item1 : items) {
                FileItem item = (FileItem) item1;

                if (item.isFormField()) {
                    //если принимаемая часть данных является полем формы
                    processFormField(item);
                } else {
                    //в противном случае рассматриваем как файл
                    File uploadedFile = processUploadedFile(item);
                    List<Error> errors = findErrors(uploadedFile);
                    File textErrors = new File("/media/damtev/Storage/Hackathon/server/diploma-validator/" +
                            "src/main/webapp/static/textErrors.html");
                    BufferedWriter writeHtml = new BufferedWriter(new FileWriter("/media/damtev/Storage/Hackathon/server/diploma-validator/" +
                            "target/validator/static/textErrors.html"));
                    writeHtml.write(begin);
                    for (Error error : errors) {
                        writeHtml.write(error.toString() + "<br>");
                    }
                    writeHtml.write(end);
                    writeHtml.close();

                    OutputStream outputStream = response.getOutputStream();
                    File file = new File(getServletContext().getRealPath("/static/textErrors.html"));
                    Files.copy(file.toPath(), outputStream);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private List<Error> findErrors(File file) throws IOException {
        return new Parser(file).findErrors();
    }

    /**
     * Сохраняет файл на сервере, в папке upload.
     * Сама папка должна быть уже создана.
     *
     * @param item
     * @throws Exception
     */
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

    /**
     * Выводит на консоль имя параметра и значение
     *
     * @param item
     */
    private void processFormField(FileItem item) {
        System.out.println(item.getFieldName() + "=" + item.getString());
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
