package com.itheima.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class DruidUtils {
    private static DataSource dataSource;

    static {
        try {
            Properties pro = new Properties();
            // 加载 resources 下的 druid.properties
            InputStream is = DruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            pro.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取数据源（你原来的方法，保留）
    public static DataSource getDataSource() {
        return dataSource;
    }

    // 建议再加一个：直接获取连接，DAO 用着更方便
    public static java.sql.Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
}