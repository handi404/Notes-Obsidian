package service.impl;

import dao.CourseDao;
import dao.impl.CourseDaoImpl;
import pojo.Course;
import service.CourseService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CourseServiceImpl implements CourseService {
    CourseDao courseDao = new CourseDaoImpl();
    @Override
    public List<Course> queryAllCourse() {
        return courseDao.SelectAllCourse();
    }

    @Override
    public List<Course> queryCourseByType(String type) {
        return courseDao.SelectCourseByType(type);
    }

    @Override
    public Course queryCourseByName(String name) {
        return courseDao.SelectCourseByName(name);
    }

    @Override
    public boolean CheckCname(String name) {
        if (courseDao.checkCname(name) != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editName(String name, String newName) {
        if (courseDao.UpdateName(name,newName) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editTypeByName(String name, String type) {
        if (courseDao.UpdateTypeByName(name,type) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean removeCourseByName(String name) {
        if (courseDao.DeleteCourseByName(name) > 0){
            return true;
        }
        return false;
    }

    @Override
    public Course queryCourseById(int id) {
        return courseDao.SelectCourseById(id);
    }

    @Override
    public List<Course> queryCourseByStudentId(int id) {
        return courseDao.SelectCourseByStudentId(id);
    }

    @Override
    public List<Course> findCourseBySid(int id) {
        return courseDao.SelectCourseBySid(id);
    }

    @Override
    public boolean addElectiveCourse(int sid, int cid) {
        if (courseDao.InsertElectiveCourse(sid,cid) > 0){
            return true;
        }
        return false;
    }
}
