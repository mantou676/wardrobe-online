package com.itheima.dao;

import com.itheima.model.User;
import com.itheima.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    // 注册：新增用户
    public int addUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_user(user_name, password, phone, address, role) VALUES(?, ?, ?, ?, ?)";
        Object[] params = {user.getUsername(), user.getPassword(), user.getPhone(), user.getAddress(), user.getRole()};
        return qr.update(sql, params);
    }

    // 根据用户名查询用户（注册判重 + 登录查找）
    public User getUserByUsername(String username) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS username, password, phone, address, role, avatar FROM t_user WHERE user_name=?";
        return qr.query(sql, new BeanHandler<>(User.class), username);
    }

    // 根据手机号查询用户（注册判重）
    public User getUserByPhone(String phone) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, phone FROM t_user WHERE phone=?";
        return qr.query(sql, new BeanHandler<>(User.class), phone);
    }

    // 根据用户id查询用户
    public User getUserById(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS username, password, phone, address, role, avatar FROM t_user WHERE id=?";
        return qr.query(sql, new BeanHandler<>(User.class), id);
    }

    // 更新用户全部信息
    public int updateUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_user SET user_name=?,password=?,phone=?,address=?,avatar=? WHERE id=?";
        Object[] params = {
                user.getUsername(),
                user.getPassword(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatar(),
                user.getId()
        };
        return qr.update(sql, params);
    }

    // 根据用户名查重
    public User isUserNameExist(String username) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS username FROM t_user WHERE user_name=?";
        return qr.query(sql, new BeanHandler<>(User.class), username);
    }

    // 根据手机号查重
    public User isPhoneExist(String phone) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, phone FROM t_user WHERE phone=?";
        return qr.query(sql, new BeanHandler<>(User.class), phone);
    }

    // 删除用户
    public int delUser(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_user WHERE id=?";
        return qr.update(sql, id);
    }

    // 后台：模糊搜索用户（用户名或手机号）
    public List<User> getUserByParam(String key) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        if (key == null || key.isEmpty()) {
            String sql = "SELECT id, user_name AS username, phone, address, role, avatar FROM t_user";
            return qr.query(sql, new BeanListHandler<>(User.class));
        }
        String sql = "SELECT id, user_name AS username, phone, address, role, avatar FROM t_user WHERE user_name LIKE ? OR phone LIKE ?";
        String like = "%" + key + "%";
        return qr.query(sql, new BeanListHandler<>(User.class), like, like);
    }
}
