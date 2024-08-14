package dao;

import pojo.Course;

import java.util.List;
import java.util.Map;

public interface CourseDao {
    public int InsertCourse(Course course);
    public int DeleteCourseById(int id);
    public int DeleteCourseByName(String name);
    public int UpdateCourse(Course course);
    public Course SelectCourseById(int id);
    public List<Course> SelectAllCourse();
    public List<Course> SelectCourseByType(String type);
    public List<Course> SelectCourseByStudentId(int id);
    public List<Course> SelectCourseBySid(int id);
    public Course SelectCourseByName(String name);
    public Course checkCname(String name);
    public int UpdateName(String name,String newName);
    public int UpdateTypeByName(String name,String type);
    public int InsertElectiveCourse(int stid,int tid);
}
