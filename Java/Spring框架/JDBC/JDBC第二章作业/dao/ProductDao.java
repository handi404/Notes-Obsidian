package JDBC第二章作业.dao;

import JDBC第二章作业.pojo.Product;

import java.util.List;

public interface ProductDao {
    //    添加
    public int insertProduct(Product product);
    //    修改
    public int updateProduct(Product product);
    //    删除
    public int deleteProductById(int id);
    //    根据id进行查询
    public Product selectProductById(int id);
    //    查询所有
    public List<Product> selectAllProduct();
}
