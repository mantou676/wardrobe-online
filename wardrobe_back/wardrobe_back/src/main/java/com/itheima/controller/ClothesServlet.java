package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.*;
import com.itheima.service.ClothesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/allClothes", "/allStyles", "/allTypes", "/clothesByName", "/clothDetails", "/addClothes", "/editClothes", "/delClothes"})
public class ClothesServlet extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();
    private ClothesService clothesService = new ClothesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        String pathInfo = req.getServletPath();
        Result result;

        try {
            switch (pathInfo) {
                case "/allTypes":
                    List<Type> typeList = clothesService.getAllTypes();
                    result = Result.success(typeList);
                    break;
                case "/allStyles":
                    List<String> styleList = clothesService.getAllStyles();
                    result = Result.success(styleList);
                    break;
                case "/allClothes":
                    String style = req.getParameter("style");
                    String type = req.getParameter("type");
                    List<Clothes> goods = clothesService.getAllClothes(style, type);
                    result = Result.success(goods);
                    break;
                case "/clothesByName":
                    String name = req.getParameter("clothesName");
                    List<Clothes> searchList = clothesService.getClothesByName(name);
                    result = Result.success(searchList);
                    break;
                case "/clothDetails":
                    int clothId = Integer.parseInt(req.getParameter("clothId"));
                    Clothes detail = clothesService.getClothDetails(clothId);
                    result = Result.success(detail);
                    break;
                default:
                    result = Result.fail("接口不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("查询失败: " + e.getMessage());
        }
        om.writeValue(pw, result);
    }

    // POST：处理新增/编辑/删除（后台管理写操作） + 兼容查询
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        String path = req.getServletPath();
        InputStream inputStream = req.getInputStream();
        Result result;
        try {
            switch (path) {
                case "/addClothes":
                    Clothes addCloth = om.readValue(inputStream, Clothes.class);
                    String addMsg = clothesService.addClothes(addCloth);
                    result = Result.success(addMsg);
                    break;
                case "/editClothes":
                    Clothes editCloth = om.readValue(inputStream, Clothes.class);
                    String editMsg = clothesService.editClothes(editCloth);
                    result = Result.success(editMsg);
                    break;
                case "/delClothes":
                    Clothes delCloth = om.readValue(inputStream, Clothes.class);
                    String delMsg = clothesService.delClothes(delCloth.getId());
                    result = Result.success(delMsg);
                    break;
                default:
                    // 非管理接口转发到doGet（兼容原有查询POST调用）
                    doGet(req, resp);
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("服装操作失败：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }
}
