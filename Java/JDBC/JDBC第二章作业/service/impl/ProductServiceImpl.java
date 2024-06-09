package JDBC第二章作业.service.impl;

import JDBC第二章作业.dao.ProductDao;
import JDBC第二章作业.dao.impl.ProductDaoImpl;
import JDBC第二章作业.pojo.Product;
import JDBC第二章作业.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductDao productDao = new ProductDaoImpl();
    @Override
    public boolean addProduct(Product product) {
        int i = productDao.insertProduct(product);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean editProduct(Product product) {
        int i = productDao.updateProduct(product);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeProductById(int id) {
        int i = productDao.deleteProductById(id);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(int id) {
        Product product = productDao.selectProductById(id);
        if (product!=null){
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getAllProduct() {
        return productDao.selectAllProduct();
    }
}
