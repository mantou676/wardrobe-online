package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Result;
import com.itheima.model.User;
import com.itheima.model.UserVo;
import com.itheima.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", urlPatterns = {"/getCurrentUser","/updateUser"})
public class UserServlet extends HttpServlet {
    private final ObjectMapper om = new ObjectMapper();
    private final UserService userService = new UserService();

    // GET：查询当前登录用户信息
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Result result;
        String path = req.getServletPath();
        try {
            if("/getCurrentUser".equals(path)){
                int userId = Integer.parseInt(req.getParameter("id"));
                User user = userService.getCurrentUser(userId);
                if(user == null){
                    result = Result.fail("用户信息不存在");
                }else{
                    // 清空密码，安全返回前端
                    user.setPassword("");
                    result = Result.success(user);
                }
            }else{
                result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("获取用户信息失败："+e.getMessage());
        }
        om.writeValue(pw, result);
    }

    // POST：修改用户信息
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Result result;
        String path = req.getServletPath();
        InputStream inputStream = req.getInputStream();
        try {
            if("/updateUser".equals(path)){
                UserVo userVo = om.readValue(inputStream, UserVo.class);
                String msg = userService.updateUser(userVo);
                result = Result.success(msg);
            }else{
                result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("修改用户信息失败："+e.getMessage());
        }
        om.writeValue(pw, result);
    }
}