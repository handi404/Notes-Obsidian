package com.yunhe.java.pojo;
/**
 * - 一张表对应于一个实体类
 * - 实体类必须实现Serializable接口
 * - 表中的一个字段对应实体类中的一个属性
 * - 属性需要进行封装，尽量不要使用基本数据类型
 * - 需要提供无参构造方法，
 * - 实体类类名和表名进行关联 ， User  ----> t_user
 * - 实体类的属性名和表中字段名进行关联， userName---->user_name,      age -------> age
 * - 开发规范：所有的实体类必须存放在entity包中（bean包，pojo）
 */

import java.io.Serializable;
//类实现Serializable接口 ，切类名和表名对应  t_person ------->Person
public class Person implements Serializable {
//    属性私有化
    private Integer pid;            //属性名和字段名对应
    private String pname;
    private Integer age;
    private String gender;
    private String mobile;
    private String address;
//  提供get/set方法
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
//  提供有参和无参的构造方法
    public Person(Integer pid, String pname, Integer age, String gender, String mobile, String address) {
        this.pid = pid;
        this.pname = pname;
        this.age = age;
        this.gender = gender;
        this.mobile = mobile;
        this.address = address;
    }

    public Person() {
    }
//    提供toString();

    @Override
    public String toString() {
        return "Person{" +
                "pid=" + pid +
                ", pname='" + pname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
