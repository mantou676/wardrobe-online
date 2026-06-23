package com.itheima.service;

import com.itheima.dao.CartDao;
import com.itheima.model.Cart;
import java.util.List;

public class CartService {
    CartDao cartDao = new CartDao();

    // 添加购物车
    public String addToCart(Cart cart) {
        try {
            return cartDao.addClothesToCart(cart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 查询用户全部购物车
    public List<Cart> getAllCartData(int userId) {
        try {
            return cartDao.allCartData(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 修改购物车商品数量
    public int updateCartData(int id, int amount) {
        try {
            return cartDao.updateClothData(id, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 删除购物车条目
    public String delCartData(int id) {
        try {
            return cartDao.delCartData(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}