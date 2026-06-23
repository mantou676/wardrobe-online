package com.itheima.dao;

import com.itheima.model.Clothes;
import com.itheima.model.Size;
import com.itheima.model.Type;
import com.itheima.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class ClothesDao {
    QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());

    //获取全部服装（筛选）
    public List<Clothes> getAllClothes(String style, String type) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT t_clothes.id,cloth_name AS clothName,image,t_type.id AS typeId,t_type.type_name AS typeName,t_clothes.style,t_clothes.price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE 1=1 ");
        List<Object> params = new java.util.ArrayList<>();
        if(style != null && !style.isEmpty()){
            sql.append(" AND t_clothes.style = ? ");
            params.add(style);
        }
        if(type != null && !type.isEmpty()){
            sql.append(" AND t_type.type_name = ? ");
            params.add(type);
        }
        return qr.query(sql.toString(),new BeanListHandler<>(Clothes.class),params.toArray());
    }

    //获取所有风格
    public List<String> getStyles() throws SQLException {
        String sql = "SELECT DISTINCT style FROM t_clothes";
        return qr.query(sql,new org.apache.commons.dbutils.handlers.ColumnListHandler<>());
    }

    //获取所有类别
    public List<Type> getTypes() throws SQLException {
        String sql = "SELECT id,type_name AS typeName FROM t_type";
        return qr.query(sql,new BeanListHandler<>(Type.class));
    }

    //模糊搜索服装名称
    public List<Clothes> getClothesByName(String name) throws SQLException {
        String sql = "SELECT t_clothes.id,cloth_name AS clothName,image,t_type.id AS typeId,t_type.type_name AS typeName,t_clothes.style,t_clothes.price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE cloth_name LIKE ?";
        return qr.query(sql,new BeanListHandler<>(Clothes.class),"%"+name+"%");
    }

    //==================== 新增详情相关方法 ====================
    //根据服装类别id查询对应尺码
    public List<Size> getSizeByType(int typeId) throws SQLException{
        String sql = "SELECT t_size.id,size_name AS sizeName FROM t_size JOIN t_type ON t_size.type_id = t_type.id WHERE t_size.type_id=?";
        List<Size> sizeList = qr.query(sql,new BeanListHandler<>(Size.class),typeId);
        return sizeList;
    }

    //根据服装id查询单条服装，并且绑定对应尺码集合
    public Clothes getClothById(int id) throws SQLException{
        String sql = "SELECT t_clothes.id,cloth_name AS clothName,image,t_type.id AS typeId,t_type.type_name AS typeName,t_clothes.style,t_clothes.price FROM t_clothes JOIN t_type ON t_clothes.type_id = t_type.id WHERE t_clothes.id=?";
        Clothes cloth = qr.query(sql,new BeanHandler<>(Clothes.class),id);
        //根据类别id查询尺码
        int typeId = cloth.getTypeId();
        List<Size> sizeList = getSizeByType(typeId);
        cloth.setSizeList(sizeList);
        return cloth;
    }
    // ==================== 后台管理新增方法（上架/编辑/删除） ====================
    // 1. 新增上架服装
    public int addClothes(Clothes clothes) throws SQLException {
        String sql = "INSERT INTO t_clothes(cloth_name,image,type_id,style,price) VALUES(?,?,?,?,?)";
        Object[] params = {
                clothes.getClothName(),
                clothes.getImage(),
                clothes.getTypeId(),
                clothes.getStyle(),
                clothes.getPrice()
        };
        return qr.update(sql, params);
    }

    // 2. 编辑修改服装
    public int editClothes(Clothes editClothes) throws SQLException {
        String sql = "UPDATE t_clothes SET cloth_name=?,image=?,type_id=?,style=?,price=? WHERE id=?";
        Object[] params = {
                editClothes.getClothName(),
                editClothes.getImage(),
                editClothes.getTypeId(),
                editClothes.getStyle(),
                editClothes.getPrice(),
                editClothes.getId()
        };
        return qr.update(sql, params);
    }

    // 3. 删除/下架服装
    public int deleteClothes(int id) throws SQLException {
        String sql = "DELETE FROM t_clothes WHERE id=?";
        return qr.update(sql, id);
    }
}