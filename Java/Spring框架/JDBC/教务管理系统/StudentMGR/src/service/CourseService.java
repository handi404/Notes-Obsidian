package service;

import pojo.Course;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public List<Course> queryAllCourse();
    public List<Course> queryCourseByType(String type);
    public Course queryCourseByName(String name);
    public boolean CheckCname(String name);
    public boolean editName(String name,String newName);
    public boolean editTypeByName(String name,String type);
    public boolean removeCourseByName(String name);
    public Course queryCourseById(int id);
    public List<Course> queryCourseByStudentId(int id);
    public List<Course> findCourseBySid(int id);
    public boolean addElectiveCourse(int sid,int cid);
}
