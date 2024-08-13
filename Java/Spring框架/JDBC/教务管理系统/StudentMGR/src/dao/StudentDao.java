package dao;

import pojo.Student;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StudentDao {
    public int InsertStudent(Student student);
    public int DeleteStudentById(int id);
    public int UpdateStudent(Student student);
    public Student SelectStudentById(int id);
    public List<Student> SelectAllStudent();
    public List<Student> SelectStudentByName(String name);
    public List<Map<String,Object>> SelectStudentByClassName(String className);
    public List<Map<String,Object>> SelectStudentByJoinTime(Date time);
    public List<Student> SelectStudentByPhone(String phone);
    public Student findStudentByUsernameAndPassword(Object[] userInfo);
    public int updateNameById(int id, String name);
    public int updateGenderById(int id,String gender);
    public int updatePasswordById(int id,String password);
    public int updateAddressById(int id,String address);
    public int updatePhoneById(int id,String Phone);
    public int updateClassIdById(int id,int classId);
}
