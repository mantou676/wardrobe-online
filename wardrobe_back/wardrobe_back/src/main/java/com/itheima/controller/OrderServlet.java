package com.itheima.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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

@WebServlet(name = "OrderServlet", urlPatterns = {"/addOrder","/getOrderByUser","/delOrderData","/payOrder","/receiveOrder","/getAllOrder","/sendOrder"})
public class OrderServlet extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();
    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Result result;
        String path = req.getServletPath();
        try {
            if("/getOrderByUser".equals(path)){
                int userId = Integer.parseInt(req.getParameter("userId"));
                int status = Integer.parseInt(req.getParameter("status"));
                List<Order> orderList = orderService.getOrderByUser(userId, status);
                result = Result.success(orderList);
            } else if ("/getAllOrder".equals(path)) {
                String userName = req.getParameter("userName");
                String statusStr = req.getParameter("status");
                Integer status = (statusStr != null && !statusStr.isEmpty()) ? Integer.parseInt(statusStr) : null;
                List<Order> allOrders = orderService.getAllOrders(userName, status);
                result = Result.success(allOrders);
            } else {
                result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("查询订单失败："+e.getMessage());
        }
        om.writeValue(pw,result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Result result;
        String path = req.getServletPath();
        InputStream inputStream = req.getInputStream();
        try {
            switch (path){
                case "/addOrder":
                    // 批量提交多订单
                    List<Order> orderList = om.readValue(inputStream,new TypeReference<List<Order>>(){});
                    String msg = "";
                    for(Order order : orderList){
                        msg = orderService.addOrder(order);
                    }
                    result = Result.success(msg);
                    break;
                case "/delOrderData":
                    Order delOrder = om.readValue(inputStream,Order.class);
                    String delMsg = orderService.delOrderData(delOrder.getId());
                    result = Result.success(delMsg);
                    break;
                case "/payOrder":
                    // 支付：0未支付 → 1待发货
                    Order payOrder = om.readValue(inputStream,Order.class);
                    int payRows = orderService.updateOrderStatus(payOrder.getId(),0,1);
                    if(payRows > 0){
                        result = Result.success("支付成功！");
                    }else {
                        result = Result.fail("支付失败！订单状态不符");
                    }
                    break;
                case "/sendOrder":
                    // 发货：1待发货 → 2已发货
                    Order sendOrder = om.readValue(inputStream, Order.class);
                    int sendRows = orderService.updateOrderStatus(sendOrder.getId(), 1, 2);
                    if (sendRows > 0) {
                        result = Result.success("发货成功！");
                    } else {
                        result = Result.fail("发货失败！订单状态不符");
                    }
                    break;
                case "/receiveOrder":
                    // 收货：2已发货 → 3已收货
                    Order recOrder = om.readValue(inputStream,Order.class);
                    int recRows = orderService.updateOrderStatus(recOrder.getId(),2,3);
                    if(recRows > 0){
                        result = Result.success("收货成功！");
                    }else {
                        result = Result.fail("收货失败！订单状态不符");
                    }
                    break;
                default:
                    result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("订单操作失败："+e.getMessage());
        }
        om.writeValue(pw,result);
    }
}