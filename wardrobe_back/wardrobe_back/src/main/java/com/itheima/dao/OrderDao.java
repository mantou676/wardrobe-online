package com.itheima.dao;

import com.itheima.model.Order;
import com.itheima.utils.DruidUtils;
import com.itheima.utils.LocalDateTimeUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDao {
    private QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());

    // 1. 添加订单
    public int addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO t_order(clothes_details,price,`status`,user_id,address,time) VALUES(?,?,?,?,?,?)";
        String dateTime = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now());
        Object[] params = {
                order.getClothesDetails(),
                order.getPrice(),
                order.getStatus(),
                order.getUserId(),
                order.getAddress(),
                dateTime
        };
        return qr.update(sql, params);
    }

    // 2. 根据用户id+状态查询订单
    public List<Order> getOrderByUser(int userId, int status) throws SQLException {
        String sql = "SELECT id,clothes_details AS clothesDetails,price,`status`,time FROM t_order WHERE user_id=? AND status=?";
        Object[] params = {userId, status};
        return qr.query(sql, new BeanListHandler<>(Order.class), params);
    }

    // 3. 删除订单：仅允许删除未支付0 / 已收货3，屏蔽待发货1、已发货2
    public int delOrderData(int id) throws SQLException {
        String sql = "DELETE FROM t_order WHERE id=? AND `status` NOT IN(1,2)";
        return qr.update(sql, id);
    }

    // 5. 后台查询全部订单（支持用户名+状态筛选）
    public List<Order> getAllOrders(String userName, Integer status) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT o.id, o.clothes_details AS clothesDetails, o.price, o.status, " +
            "o.user_id AS userId, o.address, o.time, u.user_name AS userName " +
            "FROM t_order o JOIN t_user u ON o.user_id = u.id WHERE 1=1 ");
        List<Object> params = new java.util.ArrayList<>();
        if (userName != null && !userName.isEmpty()) {
            sql.append(" AND u.user_name LIKE ? ");
            params.add("%" + userName + "%");
        }
        if (status != null) {
            sql.append(" AND o.status = ? ");
            params.add(status);
        }
        sql.append(" ORDER BY o.id DESC");
        return qr.query(sql.toString(), new BeanListHandler<>(Order.class), params.toArray());
    }

    // 4. 修改订单状态：校验原始状态再更新
    public int updateOrderStatus(int id, int initStatus, int finalStatus) throws SQLException {
        String sql = "UPDATE t_order SET status=? WHERE status=? AND id=?";
        Object[] params = {finalStatus, initStatus, id};
        return qr.update(sql, params);
    }
}