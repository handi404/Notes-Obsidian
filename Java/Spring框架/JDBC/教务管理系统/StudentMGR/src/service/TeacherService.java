package service;

import pojo.Teacher;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    public Teacher login(Object[] userInfo);
    public List<Teacher> queryAllTeacher();
    public List<Teacher> queryTeacherByName(String name);
    public List<Map<String,Object>> queryTeacherByClassName(String className);
    public List<Map<String,Object>> queryTeacherByCourseName(String courseName);
    public Teacher queryTeacherByClassIdAndCourseId(int classId,int courseId);
    public Boolean addTeacher(Teacher teacher);
    public int editTeacher(Teacher teacher);
    public boolean deleteTeacher(int id);
    public Boolean findTeacherByPhone(String phone);
    public boolean findTeacherById(int id);
    public Boolean editNameById(int id,String name);
    public Boolean editGenderById(int id,String gender);
    public Boolean editPasswordById(int id,String password);
    public Boolean editAddressById(int id,String address);
    public Boolean editCidById(int id,int Cid);
    public Boolean editPhoneById(int id,String Phone);
}
