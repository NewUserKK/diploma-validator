package com.wa285.validator.servlet;

import com.wa285.validator.parser.Fixer;
import com.wa285.validator.parser.Parser;
import com.wa285.validator.parser.errors.Error;
import com.wa285.validator.parser.errors.GlobalError;
import com.wa285.validator.parser.errors.critical.Critical;
import javafx.util.Pair;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
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

            var paragraphs = document(uploadedFile); //параграфы из загруженного документа
            ArrayList<ArrayList<Error>> errors_by_paragraph = new ArrayList<>();
            Set<Pair<String, Boolean>> common_error = new HashSet<>();

            for (String paragraph : paragraphs) {
                errors_by_paragraph.add(new ArrayList<>());
            }

            for (Error error : errors) {
                int par_number = 0;
                if (!(error instanceof GlobalError) && error.getLocation() != null) {
                    par_number = error.getLocation().getParagraphNumber();
                    errors_by_paragraph.get(par_number).add(error);
                } else {
                    boolean is_critical = error instanceof Critical;
                    common_error.add(new Pair<>(error.description, is_critical));
                }
            }

            for (int i = 0; i < paragraphs.length; i++) {
                boolean wasFontSizeError = false;
                if (paragraphs[i].isEmpty() || errors_by_paragraph.get(i).isEmpty())
                    continue;
                writeHtml.write(paragraphs[i] + "<br>");
                if (!errors_by_paragraph.get(i).isEmpty()) {
                    for (var error : errors_by_paragraph.get(i)) {
                        if (error.description.startsWith("Размер шрифта должен быть не меньше 12 пт") && wasFontSizeError)
                            continue;
                        writeHtml.write("<font color=\"red\">" + error.description + "</font><br>");
                        if (error.description.startsWith("Размер шрифта должен быть не меньше 12 пт"))
                            wasFontSizeError = true;
                    }
                }
                writeHtml.write("<br><br>");

            }

            writeHtml.write("<u>Общие ошибки</u>:<br>");
            for (var error : common_error)
                if (error.getValue())
                    writeHtml.write(error.getKey() + "<br>");

                writeHtml.write("<br><u>На это стоит обратить внимание:</u>:<br>");
                for (var error : common_error)
                    if (!error.getValue())
                        writeHtml.write(error.getKey() + "<br>");

            writeHtml.write(begin_end[1]);
            writeHtml.close();

            var fixer = new Fixer(uploadedFile, errors);
            File file_to_dow = new File(getServletContext().getRealPath("/static/download.docx"));
            fixer.writeToFile(file_to_dow);


            OutputStream outputStream = response.getOutputStream();
            File file = new File(getServletContext().getRealPath("/static/textErrors.html"));
            Files.copy(file.toPath(), outputStream);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private List<Error> findErrors(File file) throws IOException, XmlException {
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

    private String[] document(File file) throws IOException {
        List<XWPFParagraph> paragraphs = new XWPFDocument(new FileInputStream(file)).getParagraphs();
        String[] strings = new String[paragraphs.size()];
        for (int i = 0; i < paragraphs.size(); i++) {
            strings[i] = paragraphs.get(i).getText();
        }
        return strings;
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }

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
