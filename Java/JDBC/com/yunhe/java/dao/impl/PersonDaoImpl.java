package com.yunhe.java.dao.impl;

import com.yunhe.java.dao.PersonDao;
import com.yunhe.java.pojo.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {
    @Override
    public int insertPerson(Person person) {
        Connection conn = null;
        PreparedStatement pstm = null;
        int i =0;
//        jdbc6步操作
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql:///test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimeZone=Asia/shanghai";
            String user="root";
            String password = "root";
            conn = DriverManager.getConnection(url,user,password);
            String sql ="insert into t_person( p_name, age, gender, mobile, address) values (?,?,?,?,?)";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,person.getPname());
            pstm.setInt(2,person.getAge());
            pstm.setString(3,person.getGender());
            pstm.setString(4,person.getMobile());
            pstm.setString(5,person.getAddress());
            i = pstm.executeUpdate();
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    @Override
    public int updatePerson(Person person) {
        return 0;
    }

    @Override
    public int deletePersonById(int id) {
        return 0;
    }

    @Override
    public Person selectPersonById(int id) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Person person = null;
        int i =0;
//        jdbc6步操作
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql:///test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimeZone=Asia/shanghai";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
            String sql = "select p_id, p_name, age, gender, mobile, address from t_person where p_id = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,id);
            rs = pstm.executeQuery();
            while (rs.next()){
                person = new Person();
//                获取数据
                int pid = rs.getInt("p_id");
                String pname = rs.getString("p_name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String mobile =rs.getString("mobile");
                String address = rs.getString("address");
//                将数据封装到java对象
                person.setPid(pid);
                person.setPname(pname);
                person.setAge(age);
                person.setGender(gender);
                person.setMobile(mobile);
                person.setAddress(address);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return person;
    }

    @Override
    public List<Person> selectAllPerson() {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Person> personList = new ArrayList<>();

//        jdbc6步操作
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql:///test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimeZone=Asia/shanghai";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
            String sql = "select p_id, p_name, age, gender, mobile, address from t_person";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                Person person = new Person();
//                获取数据
//                int pid = rs.getInt("p_id");
//                String pname = rs.getString("p_name");
//                int age = rs.getInt("age");
//                String gender = rs.getString("gender");
//                String mobile =rs.getString("mobile");
//                String address = rs.getString("address");
//                将数据封装到java对象
                person.setPid(rs.getInt("p_id"));
                person.setPname(rs.getString("p_name"));
                person.setAge(rs.getInt("age"));
                person.setGender(rs.getString("gender"));
                person.setMobile(rs.getString("mobile"));
                person.setAddress(rs.getString("address"));
//                Person person = new Person(pid,pname,age,gender,mobile,address);
//                将对象添加到集合
                personList.add(person);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return personList;
    }
}
