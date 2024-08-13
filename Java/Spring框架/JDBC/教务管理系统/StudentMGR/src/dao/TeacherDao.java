package dao;

import pojo.Teacher;

import java.util.List;
import java.util.Map;

public interface TeacherDao {
    public int InsertTeacher(Teacher teacher);
    public int DeleteTeacherById(int id);
    public int UpdateTeacher(Teacher teacher);
    public Teacher SelectTeacherById(int id);
    public List<Teacher> SelectAllTeacher();
    public Teacher SelectTeacherByUsernameAndPassword(Object[] userInfo);
    public List<Teacher> SelectTeacherByName(String name);
    public List<Map<String,Object>> SelectTeacherByClassName(String className);
    public List<Map<String,Object>> SelectTeacherByCourseName(String courseName);
    public Teacher SelectTeacherByClassIdAndCourseId(int classId,int courseId);
    public List<Teacher> SelectTeacherByPhone(String phone);
    public int updateNameById(int id, String name);
    public int updateGenderById(int id,String gender);
    public int updatePasswordById(int id,String password);
    public int updateAddressById(int id,String address);
    public int updateCidById(int id,int cid);
    public int updatePhoneById(int id,String Phone);
}
