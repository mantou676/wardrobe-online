package com.itheima.service;

import com.itheima.dao.CartDao;
import com.itheima.dao.OrderDao;
import com.itheima.model.Order;
import java.util.List;

public class OrderService {
    private OrderDao orderDao = new OrderDao();
    private CartDao cartDao = new CartDao();

    // 提交订单，成功后清空该用户全部购物车
    public String addOrder(Order order) {
        try {
            int rows = orderDao.addOrder(order);
            if(rows > 0){
                // 下单成功清空购物车
                cartDao.delUserCartData(order.getUserId());
                return "订单提交成功！";
            }
            return "订单提交失败！";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // 根据用户id+状态查询订单列表
    public List<Order> getOrderByUser(int userId, int status) {
        try {
            return orderDao.getOrderByUser(userId, status);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // 删除订单
    public String delOrderData(int id) {
        try {
            int rows = orderDao.delOrderData(id);
            return rows > 0 ? "删除成功！" : "删除失败！";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // 后台查询全部订单（支持用户名+状态筛选）
    public List<Order> getAllOrders(String userName, Integer status) {
        try {
            return orderDao.getAllOrders(userName, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 修改订单状态（支付/收货共用）
    public int updateOrderStatus(int id, int initStatus, int finalStatus) {
        try {
            return orderDao.updateOrderStatus(id, initStatus, finalStatus);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}