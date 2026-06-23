package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Result;
import com.itheima.model.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/register", "/login"})
public class LoginOrRegServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=utf-8");
            String action = request.getServletPath();
            InputStream inputStream = request.getInputStream();
            ObjectMapper om = new ObjectMapper();
            PrintWriter out = response.getWriter();
            Result result;

            if ("/register".equals(action)) {
                User user = om.readValue(inputStream, User.class);
                // Service还是返回String，完全不改service
                String msg = userService.register(user);
                // 判断字符串封装Result
                if("ok".equals(msg)){
                    result = Result.success(null);
                }else{
                    result = Result.fail(msg);
                }
            } else if ("/login".equals(action)) {
                User user = om.readValue(inputStream, User.class);
                String msg = userService.login(user);
                if("ok".equals(msg)){
                    // 登录成功，返回用户数据（含id）+ JWT token
                    User loginUser = userService.getUserByUsername(user.getUsername());
                    loginUser.setPassword(null);
                    String token = JwtUtils.createToken(loginUser.getId());
                    Map<String, Object> data = new HashMap<>();
                    data.put("user", loginUser);
                    data.put("token", token);
                    result = Result.success(data);
                }else{
                    result = Result.fail(msg);
                }
            } else {
                result = Result.fail("接口不存在");
            }
            om.writeValue(out, result);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Result err = Result.fail("服务器内部错误");
                new ObjectMapper().writeValue(response.getWriter(), err);
            } catch (Exception ignored) {
            }
        }
    }
}