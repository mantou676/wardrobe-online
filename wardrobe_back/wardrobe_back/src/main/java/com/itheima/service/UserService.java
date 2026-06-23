package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.model.User;
import com.itheima.model.UserVo;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    UserDao userDao = new UserDao();

    // 注册
    public String register(User user) {
        // 1. 简单校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }

        try {
            // 2. 判断用户名是否重复
            if (userDao.getUserByUsername(user.getUsername()) != null) {
                return "用户名已存在";
            }
            // 3. 判断手机号是否重复
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (userDao.getUserByPhone(user.getPhone()) != null) {
                    return "手机号已存在";
                }
            }
            // 4. 设置为普通用户
            user.setRole(2);
            // 5. 保存到数据库
            userDao.addUser(user);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
            return "注册失败: " + e.getMessage();
        }
    }

    // 登录
    public String login(User user) {
        // 1. 简单校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }

        try {
            // 2. 根据用户名查找用户
            User existUser = userDao.getUserByUsername(user.getUsername());
            if (existUser == null) {
                return "用户名或密码错误";
            }
            // 3. 比对密码
            if (!existUser.getPassword().equals(user.getPassword())) {
                return "用户名或密码错误";
            }
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
            return "登录失败: " + e.getMessage();
        }
    }

    // 根据用户名获取用户信息（登录成功后返回给前端）
    public User getUserByUsername(String username) {
        try {
            return userDao.getUserByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ========== 个人中心新增方法 ==========
    // 根据id获取当前登录用户
    public User getCurrentUser(int id) {
        try {
            return userDao.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 更新用户信息（密码可选：为空则只更新基本信息，不校验密码）
    public String updateUser(UserVo userVo) {
        try {
            // 1. 查询数据库当前用户
            User currentUser = this.getCurrentUser(userVo.getId());
            if (currentUser == null) {
                return "用户不存在！";
            }
            // 2. 判断是否要修改密码：传了原密码才走密码校验流程
            String oldPwd = userVo.getPassword();
            boolean isChangingPwd = (oldPwd != null && !oldPwd.trim().isEmpty());
            if (isChangingPwd) {
                // 校验原始密码
                if (!currentUser.getPassword().equals(oldPwd)) {
                    return "原始密码输入错误，请重新输入！";
                }
                // 新密码为空则沿用旧密码
                if (userVo.getNewpsw() != null && !userVo.getNewpsw().trim().isEmpty()) {
                    userVo.setPassword(userVo.getNewpsw());
                }
            } else {
                // 不修改密码，沿用数据库中的旧密码
                userVo.setPassword(currentUser.getPassword());
            }
            // 3. 校验用户名是否被其他用户占用
            User nameExist = userDao.isUserNameExist(userVo.getUsername());
            if (nameExist != null && !nameExist.getId().equals(userVo.getId())) {
                return "用户名已存在，请输入其他用户名！";
            }
            // 4. 校验手机号是否被其他用户占用
            User phoneExist = userDao.isPhoneExist(userVo.getPhone());
            if (phoneExist != null && !phoneExist.getId().equals(userVo.getId())) {
                return "手机号已存在，请确认！";
            }
            // 5. 执行更新
            int rows = userDao.updateUser(userVo);
            return rows > 0 ? "修改成功！" : "修改失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库异常，修改失败！";
        }
    }

    // 后台：模糊搜索用户
    public List<User> getUserByParam(String key) {
        try {
            return userDao.getUserByParam(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 后台：删除用户
    public String delUser(int id) {
        try {
            int rows = userDao.delUser(id);
            return rows > 0 ? "删除成功！" : "删除失败！";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}