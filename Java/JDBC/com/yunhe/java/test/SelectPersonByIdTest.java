package com.yunhe.java.test;

import com.yunhe.java.dao.PersonDao;
import com.yunhe.java.dao.impl.PersonDaoImpl;
import com.yunhe.java.pojo.Person;

public class SelectPersonByIdTest {
    public static void main(String[] args) {
        PersonDao personDao = new PersonDaoImpl();
        Person p = personDao.selectPersonById(10001);
        System.out.println(p);
    }
}
