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
import java.util.List;

@WebServlet(name = "ManageUserServlet", urlPatterns = {
        "/getAllUser",
        "/searchUser",
        "/delUser",
        "/editUser",
        "/addUser"
})
public class ManageUserServlet extends HttpServlet {
    private final ObjectMapper om = new ObjectMapper();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        String path = req.getServletPath();
        Result result;
        try {
            if ("/getAllUser".equals(path)) {
                List<User> allUser = userService.getUserByParam(null);
                result = Result.success(allUser);
            } else if ("/searchUser".equals(path)) {
                String key = req.getParameter("nameOrPhone");
                List<User> list = userService.getUserByParam(key);
                result = Result.success(list);
            } else {
                result = Result.fail("接口不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("用户查询异常：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        InputStream inputStream = req.getInputStream();
        String path = req.getServletPath();
        Result result;
        try {
            switch (path) {
                case "/delUser":
                    User delUser = om.readValue(inputStream, User.class);
                    String delMsg = userService.delUser(delUser.getId());
                    result = Result.success(delMsg);
                    break;
                case "/editUser":
                    UserVo uv = om.readValue(inputStream, UserVo.class);
                    User oldUser = userService.getCurrentUser(uv.getId());
                    uv.setPassword(oldUser.getPassword());
                    uv.setNewpsw(uv.getPassword());
                    String editMsg = userService.updateUser(uv);
                    result = Result.success(editMsg);
                    break;
                case "/addUser":
                    User addUser = om.readValue(inputStream, User.class);
                    String addMsg = userService.register(addUser);
                    if ("ok".equals(addMsg)) {
                        result = Result.success("添加用户成功！");
                    } else {
                        result = Result.fail(addMsg);
                    }
                    break;
                default:
                    result = Result.fail("接口不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("用户操作异常：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }
}
