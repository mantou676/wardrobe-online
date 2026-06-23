package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Order;
import com.itheima.model.Result;
import com.itheima.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ManageOrderServlet", urlPatterns = {
        "/allOrderData",
        "/searchOrder",
        "/deliveryOrder"
})
public class ManageOrderServlet extends HttpServlet {
    private final ObjectMapper om = new ObjectMapper();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        String path = req.getServletPath();
        Result result;
        try {
            if ("/allOrderData".equals(path)) {
                List<Order> list = orderService.getAllOrders(null, null);
                result = Result.success(list);
            } else if ("/searchOrder".equals(path)) {
                String userName = req.getParameter("userName");
                String statusStr = req.getParameter("status");
                Integer status = null;
                if (statusStr != null && !statusStr.isEmpty()) {
                    status = Integer.parseInt(statusStr);
                }
                List<Order> list = orderService.getAllOrders(userName, status);
                result = Result.success(list);
            } else {
                result = Result.fail("接口不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("订单查询异常：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        InputStream inputStream = req.getInputStream();
        Result result;
        try {
            if ("/deliveryOrder".equals(req.getServletPath())) {
                Order order = om.readValue(inputStream, Order.class);
                int rows = orderService.updateOrderStatus(order.getId(), 1, 2);
                if (rows > 0) {
                    result = Result.success("发货成功！");
                } else {
                    result = Result.fail("发货失败！订单状态不符");
                }
            } else {
                result = Result.fail("接口不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.fail("发货操作异常：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }
}
