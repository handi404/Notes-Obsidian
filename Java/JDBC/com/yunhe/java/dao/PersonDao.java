package com.yunhe.java.dao;

import com.yunhe.java.pojo.Person;

import java.util.List;

/**
 * 在dao接口中定义对表的所有操作
 */
public interface PersonDao {
//    添加动作
    public int insertPerson(Person person);
//    修改动作
    public int updatePerson(Person person);
//    删除动作
    public int deletePersonById(int id);
//    根据id查询
    public Person selectPersonById(int id);
//    查询所有数据
    public List<Person> selectAllPerson();
}
