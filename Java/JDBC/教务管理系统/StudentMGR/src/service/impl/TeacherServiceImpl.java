package service.impl;

import dao.TeacherDao;
import dao.impl.TeacherDaoImpl;
import pojo.Teacher;
import service.TeacherService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TeacherServiceImpl implements TeacherService {
    TeacherDao teacherDao = new TeacherDaoImpl();
    @Override
    public Teacher login(Object[] userInfo) {
        return teacherDao.SelectTeacherByUsernameAndPassword(userInfo);
    }
    @Override
    public List<Teacher> queryAllTeacher(){
        return teacherDao.SelectAllTeacher();
    }
    public List<Teacher> queryTeacherByName(String name){
        return teacherDao.SelectTeacherByName(name);
    }
    public List<Map<String,Object>> queryTeacherByClassName(String className){
        return teacherDao.SelectTeacherByClassName(className);
    }

    @Override
    public List<Map<String, Object>> queryTeacherByCourseName(String courseName) {
        return teacherDao.SelectTeacherByCourseName(courseName);
    }

    @Override
    public Teacher queryTeacherByClassIdAndCourseId(int classId, int courseId) {
        return teacherDao.SelectTeacherByClassIdAndCourseId(classId,courseId);
    }

    @Override
    public Boolean addTeacher(Teacher teacher){
        if (teacherDao.InsertTeacher(teacher)>0){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public int editTeacher(Teacher teacher){
        return teacherDao.UpdateTeacher(teacher);
    }
    @Override
    public boolean deleteTeacher(int id){
        if (teacherDao.DeleteTeacherById(id)>0){
            return true;
        }else {
            return false;
        }
    }
    public Boolean findTeacherByPhone(String phone){
        List<Teacher> teacher = teacherDao.SelectTeacherByPhone(phone);
        if(teacher!=null){
            return true;
        }
        return false;
    }

    @Override
    public boolean findTeacherById(int id) {
        if (teacherDao.SelectTeacherById(id) != null){
            return true;
        }else {
            return false;
        }
    }

    public Boolean editNameById(int id,String name){
        if (teacherDao.updateNameById(id,name)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean editGenderById(int id, String gender) {
        if (teacherDao.updateGenderById(id,gender)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean editPasswordById(int id, String password) {
        if (teacherDao.updatePasswordById(id,password)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean editAddressById(int id, String address) {
        if (teacherDao.updateAddressById(id,address)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean editCidById(int id, int Cid) {
        if (teacherDao.updateCidById(id,Cid)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean editPhoneById(int id, String Phone) {
        if (teacherDao.updatePhoneById(id,Phone)>0){
            return true;
        }else {
            return false;
        }
    }
}
