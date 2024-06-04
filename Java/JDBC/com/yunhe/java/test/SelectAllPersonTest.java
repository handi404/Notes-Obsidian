package com.yunhe.java.test;

import com.yunhe.java.dao.PersonDao;
import com.yunhe.java.dao.impl.PersonDaoImpl;
import com.yunhe.java.pojo.Person;

import java.util.List;

public class SelectAllPersonTest {
    public static void main(String[] args) {
        PersonDao personDao = new PersonDaoImpl();
        List<Person> list =personDao.selectAllPerson();
        for(Person p:list){
            System.out.println(p);
        }
    }
}
