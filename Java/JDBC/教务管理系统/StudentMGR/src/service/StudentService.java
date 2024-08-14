package service;

import pojo.Student;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StudentService {
    public Student login(Object[] userInfo);
    public List<Student> queryAllStudent();
    public List<Student> queryStudentByName(String name);
    public List<Map<String,Object>> queryStudentByClassName(String className);
    public List<Map<String,Object>> queryStudentByJoinTime(Date time);
    public boolean findStudentByPhone(String phone);
    public boolean addStudent(Student student);
    public boolean editNameById(int id,String name);
    public boolean editGenderById(int id,String gender);
    public boolean editPasswordById(int id,String password);
    public boolean editAddressById(int id,String address);
    public boolean editClassIdById(int id,int ClassId);
    public boolean editPhoneById(int id,String Phone);
    public boolean deleteStudent(int id);
}
