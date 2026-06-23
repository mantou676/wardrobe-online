package com.itheima.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(urlPatterns = "/uploadFile")
@MultipartConfig
public class UploadFileServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain;charset=utf-8");
        // 直接保存到前端 wardrobe_front/images 目录，确保前端能显示
        String frontImgPath = "D:/app/develop/javaweb_end_test/wardrobe_front/images";
        File frontDir = new File(frontImgPath);
        if (!frontDir.exists()) frontDir.mkdirs();

        Part part = req.getPart("clothesImage");
        String originFileName = part.getSubmittedFileName();
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFileName = timeStamp + "_" + originFileName;
        File targetFile = new File(frontDir, newFileName);
        part.write(targetFile.getAbsolutePath());
        System.out.println("图片已保存到: " + targetFile.getAbsolutePath());

        resp.getWriter().write(newFileName);
    }
}