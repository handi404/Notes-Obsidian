package JDBC第二章作业.dao.impl;

import JDBC第二章作业.dao.ProductDao;
import JDBC第二章作业.pojo.Product;
import JDBC第二章作业.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public int insertProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstm = null;
        int i =0;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "insert into t_product(product_name, price, brand, address, production_date, specifications, manufacturer) values (?,?,?,?,?,?,?)";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,product.getProductName());
            pstm.setDouble(2,product.getPrice());
            pstm.setString(3,product.getBrand());
            pstm.setString(4,product.getAddress());
            pstm.setString(5,product.getProductionDate());
            pstm.setString(6,product.getSpecifications());
            pstm.setString(7,product.getManufacturer());
            i = pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(conn,pstm);
        }
        return i;
    }

    @Override
    public int updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstm = null;
        int i = 0;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "update t_product set product_name = ?, price = ?, brand = ?, address = ?, production_date = ?, specifications = ?, manufacturer = ? where p_id = ?";
            pstm = conn.prepareCall(sql);
            pstm.setString(1,product.getProductName());
            pstm.setDouble(2,product.getPrice());
            pstm.setString(3,product.getBrand());
            pstm.setString(4,product.getAddress());
            pstm.setString(5,product.getProductionDate());
            pstm.setString(6,product.getSpecifications());
            pstm.setString(7,product.getManufacturer());
            pstm.setInt(8,product.getPid());
            i = pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            orm.utils.JdbcUtils.close(conn,pstm);
        }

        return i;
    }

    @Override
    public int deleteProductById(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        int i = 0;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "delete from t_product where p_id = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,id);
            i = pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            orm.utils.JdbcUtils.close(conn,pstm);
        }

        return i;
    }

    @Override
    public Product selectProductById(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        Product product = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "select p_id, product_name, price, brand, address, production_date, specifications, manufacturer from t_product where p_id = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,id);
            rs = pstm.executeQuery();
            while (rs.next()){
                product = new Product();
                product.setPid(rs.getInt("p_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setBrand(rs.getString("brand"));
                product.setAddress(rs.getString("address"));
                product.setProductionDate(rs.getString("production_date"));
                product.setSpecifications(rs.getString("specifications"));
                product.setManufacturer(rs.getString("manufacturer"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            orm.utils.JdbcUtils.close(conn,pstm,rs);
        }

        return product;
    }

    @Override
    public List<Product> selectAllProduct() {
        Connection conn = null;
        PreparedStatement pstm = null;
        List<Product> productList = new ArrayList<>();
        Product product = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "select p_id, product_name, price, brand, address, production_date, specifications, manufacturer from t_product";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                product = new Product();
                product.setPid(rs.getInt("p_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setBrand(rs.getString("brand"));
                product.setAddress(rs.getString("address"));
                product.setProductionDate(rs.getString("production_date"));
                product.setSpecifications(rs.getString("specifications"));
                product.setManufacturer(rs.getString("manufacturer"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            orm.utils.JdbcUtils.close(conn,pstm,rs);
        }

        return productList;
    }
}
