package com.itheima.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.UserDao;
import com.itheima.model.Result;
import com.itheima.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// 只拦截购物车、订单Servlet，需要登录才能访问
@WebFilter(servletNames = {"CartServlet", "OrderServlet","UserServlet","ClothesServlet"})
public class AccessFilter implements Filter {
    private UserDao userDao = new UserDao();
    private ObjectMapper om = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 统一返回JSON格式
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();

        // 从请求头获取token
        String token = request.getHeader("token");
        if (token == null || token.trim().length() == 0) {
            // 无token，未登录，拦截
            om.writeValue(pw, Result.fail("暂无权限，请先登录"));
            return;
        }

        try {
            // 解析token获取用户id
            String userIdStr = JWT.decode(token).getAudience().get(0);
            int userId = Integer.parseInt(userIdStr);
            // 查询用户是否存在
            User loginUser = userDao.getUserById(userId);
            if (loginUser == null) {
                om.writeValue(pw, Result.fail("用户不存在，请重新登录"));
                return;
            }
            // token有效，放行访问购物车/订单接口
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // token过期/篡改，校验失败
            om.writeValue(pw, Result.fail("登录失效，请重新登录"));
        }
    }
}