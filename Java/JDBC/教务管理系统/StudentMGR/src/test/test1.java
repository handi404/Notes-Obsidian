package test;

import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class test1 {
    public static void main(String[] args) throws SQLException {
        Connection conn = JdbcUtils.getConnection();
        String sql = "insert into course(cname, type) VALUES (软,1)";
        PreparedStatement pstm = conn.prepareStatement(sql);
        int i = pstm.executeUpdate();
        System.out.println(i);
    }
}
