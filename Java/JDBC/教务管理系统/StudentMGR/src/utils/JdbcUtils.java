package utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtils {
    //    创建一个数据库连接池，默认加载src目录下c3p0-config.xml文件
    private static ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
    //    创建一个用来获取数据库链接的方法
    public static Connection getConnection(){
        try {
//            从数据库链接池获取一个链接对象
            return pooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(pooledDataSource);
        return jdbcTemplate;
    }
}

