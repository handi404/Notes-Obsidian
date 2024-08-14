package JDBC第二章作业.pojo;
//创建商品表t_product 字段 p_id, product_name, price(价格), brand(品牌), address(地址),
// production_date (生产日期), specifications（规格） manufacturer（厂家）
// 根据t_product表创建对应的实体类

public class Product {
    private Integer pid;
    private String productName;
    private Double price;
    private String brand;
    private String address;
    private String productionDate;
    private String specifications;
    private String manufacturer;

    public Product() {
    }
    public Product(Integer pid, String productName, Double price, String brand, String address, String productionDate, String specifications, String manufacturer) {
        this.pid = pid;
        this.productName = productName;
        this.price = price;
        this.brand = brand;
        this.address = address;
        this.productionDate = productionDate;
        this.specifications = specifications;
        this.manufacturer = manufacturer;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", brand='" + brand + '\'' +
                ", address='" + address + '\'' +
                ", productionDate='" + productionDate + '\'' +
                ", specifications='" + specifications + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
