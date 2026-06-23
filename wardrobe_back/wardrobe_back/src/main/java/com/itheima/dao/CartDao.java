package com.itheima.dao;

import com.itheima.model.Cart;
import com.itheima.model.Clothes;
import com.itheima.model.User;
import com.itheima.utils.DruidUtils;
import com.itheima.utils.LocalDateTimeUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CartDao {
    // 1. 添加商品到购物车：同用户+同服装+同尺码则数量+1，无则新增
    public String addClothesToCart(Cart cart) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_cart(`cloth_id`,cloth_size,amount,user_id,date) VALUES(?,?,?,?,?)";
        // 查询当前用户该服装+尺码是否已存在购物车
        Cart cartParam = clothesAmount(cart.getUserId(), cart.getClothId(), cart.getClothSize());
        if (cartParam == null) {
            // 不存在，新增记录，数量默认1
            String dateTime = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now());
            Object[] params = {cart.getClothId(), cart.getClothSize(), 1, cart.getUserId(), dateTime};
            int addCount = qr.update(sql, params);
            return addCount > 0 ? "添加成功！" : "添加失败";
        } else {
            // 已存在，原有数量+1
            int count = updateClothData(cartParam.getId(), cartParam.getAmount() + 1);
            return count > 0 ? "添加成功！" : "添加失败";
        }
    }

    // 2. 根据用户id、服装id、尺码查询购物车单条记录（判断是否重复）
    public Cart clothesAmount(int userId, int clothId, String clothSize) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id,cloth_id AS clothId,cloth_size AS clothSize,amount,user_id AS userId,date " +
                "FROM t_cart WHERE user_id=? and cloth_id=? and cloth_size=?";
        Object[] params = {userId, clothId, clothSize};
        Cart cart = qr.query(sql, new BeanHandler<>(Cart.class), params);
        System.out.println(cart);
        return cart;
    }

    // 3. 修改购物车商品数量
    public int updateClothData(int id, int amount) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_cart SET amount=? WHERE id=?";
        Object[] params = {amount, id};
        return qr.update(sql, params);
    }

    // 4. 根据用户id查询全部购物车数据，自动封装关联服装、用户完整信息
    public List<Cart> allCartData(int userId) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id,cloth_id AS clothId,cloth_size AS clothSize,amount,user_id AS userId,date " +
                "FROM t_cart WHERE user_id=?";
        List<Cart> allData = qr.query(sql, new BeanListHandler<>(Cart.class), userId);
        // 循环封装关联的服装、用户对象
        ClothesDao clothesDao = new ClothesDao();
        UserDao userDao = new UserDao();
        for (Cart data : allData) {
            Clothes clothes = clothesDao.getClothById(data.getClothId());
            data.setClothes(clothes);
            User user = userDao.getUserById(data.getUserId());
            data.setUser(user);
        }
        return allData;
    }

    // 5. 根据购物车id删除购物车条目（文档下方补充删除方法）
    public String delCartData(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_cart WHERE id=?";
        int count = qr.update(sql, id);
        return count < 1 ? "删除失败！" : "删除成功！";
    }
    // 删除当前用户全部购物车数据（结算下单后清空购物车）
    public int delUserCartData(int userId) throws SQLException{
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_cart WHERE user_id=?";
        return qr.update(sql,userId);
    }
}