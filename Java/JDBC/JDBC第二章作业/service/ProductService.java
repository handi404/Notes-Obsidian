package JDBC第二章作业.service;
import JDBC第二章作业.pojo.Product;

import java.util.List;

public interface ProductService {
    public boolean addProduct(Product product);
    public boolean editProduct(Product product);
    public boolean removeProductById(int id);
    public Product getProductById(int id);
    public List<Product> getAllProduct();
}
