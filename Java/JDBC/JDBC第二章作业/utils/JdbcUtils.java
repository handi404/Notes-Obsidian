package JDBC第二章作业.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {
    //    添加全局的ThreadLocal对象
    private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    static Properties properties = new Properties();
    static String driverClass;
    static String url;
    static String user;
    static String password;
    //    静态代码块：随着类的加载而加载，且类加载完成之后会自动执行，类只会加载一次，静态代码块只会执行一次
    static {
        try {
//            读取配置文件
            InputStream input = new FileInputStream("jdbc.properties");
//            使用属性对象，加载文件流对象，
            properties.load(input);
//            获取配置文件中的数据
            driverClass = properties.getProperty("driverClass");
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
//            加载驱动
            Class.forName(driverClass);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn=tl.get();
        try {
            if(conn==null){
                conn  = DriverManager.getConnection(url,user,password);
            }
            tl.set(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn, PreparedStatement pstm) {
        if(pstm!=null){
            try {
                pstm.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
                tl.remove();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void close(Connection conn, PreparedStatement pstm, ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(pstm!=null){
            try {
                pstm.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
                tl.remove();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
