package com.itheima.service;

import com.itheima.dao.ClothesDao;
import com.itheima.model.Clothes;
import com.itheima.model.Type;
import java.util.List;

public class ClothesService {
    ClothesDao clothesDao = new ClothesDao();

    //获取所有服装（筛选）
    public List<Clothes> getAllClothes(String style, String type) {
        try {
            return clothesDao.getAllClothes(style, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取所有服装风格
    public List<String> getAllStyles() {
        try {
            return clothesDao.getStyles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取所有服装类别
    public List<Type> getAllTypes() {
        try {
            return clothesDao.getTypes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //模糊搜索服装名称
    public List<Clothes> getClothesByName(String name) {
        try {
            return clothesDao.getClothesByName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //==================== 新增：服装详情接口 ====================
    public Clothes getClothDetails(int id) {
        try {
            return clothesDao.getClothById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // ==================== 后台管理新增业务方法 ====================
    // 上架新增服装
    public String addClothes(Clothes clothes) {
        try {
            int count = clothesDao.addClothes(clothes);
            return count > 0 ? "上架成功" : "上架失败";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 修改服装信息
    public String editClothes(Clothes editClothes) {
        try {
            int count = clothesDao.editClothes(editClothes);
            return count > 0 ? "修改成功！" : "修改失败！";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 删除下架服装
    public String delClothes(int id) {
        try {
            int count = clothesDao.deleteClothes(id);
            return count > 0 ? "下架服装成功！" : "下架失败！";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}