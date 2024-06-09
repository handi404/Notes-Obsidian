package JDBC第二章作业.view;
import JDBC第二章作业.pojo.Product;
import JDBC第二章作业.service.ProductService;
import JDBC第二章作业.service.impl.ProductServiceImpl;

import java.util.Scanner;

/*
p_id, product_name, price(价格), brand(品牌), address(地址),
production_date (生产日期), specifications（规格） manufacturer（厂家）
 */
public class AddProductView {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入商品名");
        String productName = sc.next();
        System.out.println("请输入商品价格");
        Double price = sc.nextDouble();
        System.out.println("请输入商品品牌");
        String brand = sc.next();
        System.out.println("请输入商品地址");
        String address = sc.next();
        System.out.println("请输入商品生产日期");
        String productionDate = sc.next();
        System.out.println("请输入商品规格");
        String specifications = sc.next();
        System.out.println("请输入商品厂家");
        String manufacturer = sc.next();
        Product product = new Product(null,productName,price,brand,address,productionDate,specifications,manufacturer);
        ProductService productService = new ProductServiceImpl();
        if(productService.addProduct(product)){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }
    }
}
