package service.impl;

import dao.StudentDao;
import dao.impl.StudentDaoImpl;
import pojo.Student;
import service.StudentService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StudentServiceImpl implements StudentService {
    private StudentDao studentDao = new StudentDaoImpl();
    @Override
    public Student login(Object[] userInfo) {
        return studentDao.findStudentByUsernameAndPassword(userInfo);
    }

    @Override
    public List<Student> queryAllStudent() {
        return studentDao.SelectAllStudent();
    }

    @Override
    public List<Student> queryStudentByName(String name) {
        return studentDao.SelectStudentByName(name);
    }

    @Override
    public List<Map<String, Object>> queryStudentByClassName(String className) {
        return studentDao.SelectStudentByClassName(className);
    }

    @Override
    public List<Map<String, Object>> queryStudentByJoinTime(Date time) {
        return studentDao.SelectStudentByJoinTime(time);
    }

    @Override
    public boolean findStudentByPhone(String phone) {
        List<Student> studentList = studentDao.SelectStudentByPhone(phone);
        if (studentList!=null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addStudent(Student student) {
        if (studentDao.InsertStudent(student)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editNameById(int id, String name) {
        if (studentDao.updateNameById(id,name)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editGenderById(int id, String gender) {
        if (studentDao.updateGenderById(id,gender)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editPasswordById(int id, String password) {
        if (studentDao.updatePasswordById(id,password)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editAddressById(int id, String address) {
        if (studentDao.updateAddressById(id,address)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editClassIdById(int id, int ClassId) {
        if (studentDao.updateClassIdById(id,ClassId)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editPhoneById(int id, String Phone) {
        if (studentDao.updatePhoneById(id,Phone)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int id) {
        if (studentDao.DeleteStudentById(id)>0){
            return true;
        }else {
            return false;
        }
    }
}
