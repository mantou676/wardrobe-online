package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Cart;
import com.itheima.model.Result;
import com.itheima.service.CartService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/addToCart","/getCartDataByUser","/updateCartData","/delCartData"})
public class CartServlet extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();
    private CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Result result;
        String path = req.getServletPath();
        try {
            if("/getCartDataByUser".equals(path)){
                // 查询用户购物车列表
                int userId = Integer.parseInt(req.getParameter("userId"));
                List<Cart> cartList = cartService.getAllCartData(userId);
                result = Result.success(cartList);
            }else{
                result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("查询购物车失败：" + e.getMessage());
        }
        om.writeValue(pw, result);
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
                case "/addToCart":
                    Cart addCart = om.readValue(inputStream, Cart.class);
                    String addMsg = cartService.addToCart(addCart);
                    result = Result.success(addMsg);
                    break;
                case "/updateCartData":
                    Cart updateCart = om.readValue(inputStream, Cart.class);
                    int updateRow = cartService.updateCartData(updateCart.getId(), updateCart.getAmount());
                    result = Result.success(updateRow);
                    break;
                case "/delCartData":
                    Cart delCart = om.readValue(inputStream, Cart.class);
                    String delMsg = cartService.delCartData(delCart.getId());
                    result = Result.success(delMsg);
                    break;
                default:
                    result = Result.fail("接口不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("操作购物车失败：" + e.getMessage());
        }
        om.writeValue(pw, result);
    }
}