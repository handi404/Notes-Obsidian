package com.yunhe.java.test;

import com.yunhe.java.dao.PersonDao;
import com.yunhe.java.dao.impl.PersonDaoImpl;
import com.yunhe.java.pojo.Person;

public class InsertPersonTest {
    public static void main(String[] args) {
//        1.创建dao对象
        PersonDao personDao = new PersonDaoImpl();
//
        Person p1 = new Person(null,"武松",25,"男","10087","梁山");
        int i = personDao.insertPerson(p1);
        if(i>0){
            System.out.println("添加成功");
        }else{
            System.out.printf("添加失败");
        }
    }
}
