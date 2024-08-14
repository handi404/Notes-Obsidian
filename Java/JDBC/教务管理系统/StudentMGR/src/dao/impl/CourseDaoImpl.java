package dao.impl;

import dao.CourseDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Course;
import utils.JdbcUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CourseDaoImpl implements CourseDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertCourse(Course course) {
        String sql = "insert into course(cname,type) values(?,?)";
        int i = jdbcTemplate.update(sql, course.getCname(), course.getType());
        return i;
    }

    @Override
    public int DeleteCourseById(int id) {
        String sql = "delete from course where id = ?";
        int i = jdbcTemplate.update(sql, id);
        return i;
    }

    @Override
    public int DeleteCourseByName(String name) {
        String sql = "delete from course where cname  =?";
        return jdbcTemplate.update(sql,name);
    }

    @Override
    public int UpdateCourse(Course course) {
        String sql = "update course set cname = ?,type = ? where id = ?";
        int i = jdbcTemplate.update(sql, course.getCname(), course.getType(), course.getId());
        return i;
    }

    @Override
    public Course SelectCourseById(int id) {
        String sql = "select * from course where id = ?";
        List<Course> courseList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Course>(Course.class), id);
        if (courseList.isEmpty()){
            return null;
        }
        return courseList.get(0);
    }

    @Override
    public List<Course> SelectAllCourse() {
        String sql = "select * from course";
        List<Course> courseList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Course>(Course.class));
        return courseList;
    }

    @Override
    public List<Course> SelectCourseByType(String type) {
        String sql = "select * from course where type = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),type);
    }

    @Override
    public List<Course> SelectCourseByStudentId(int id) {
        String sql = "select c.* from student s,teacher t,course c,electivecourse e where s.id = e.stid and e.tid = t.id and t.cid = c.id and s.id = ?";
        List<Course> courseList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),id);
        if (courseList.size()==0){
            return null;
        }
        return courseList;
    }

    @Override
    public List<Course> SelectCourseBySid(int id) {
        String sql = "Select c.* as id from student s,teacher t,teachingclass tc,course c where s.classid = tc.classid and tc.tid = t.id and t.cid = c.id and s.id = ?";
        List<Course> courseList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),id);
        if (courseList.size()==0){
            return null;
        }
        return courseList;
    }

    @Override
    public Course SelectCourseByName(String name) {
        String sql = "select * from course where cname = ?";
        List<Course> courseList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),name);
        return courseList.get(0);
    }


    @Override
    public Course checkCname(String name) {
        String sql = "select * from course where cname = ?";
        List<Course> courseList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Course.class),name);
        return courseList.get(0);
    }

    @Override
    public int UpdateName(String name, String newName) {
        String sql = "update course set cname = ? where cname = ?";
        return jdbcTemplate.update(sql,newName,name);
    }

    @Override
    public int UpdateTypeByName(String name, String type) {
        String sql = "update course set type = ? where cname = ?";
        return jdbcTemplate.update(sql,type,name);
    }

    @Override
    public int InsertElectiveCourse(int stid, int tid) {
        String sql = "insert into electivecourse(stid,tid) values(?,?)";
        return jdbcTemplate.update(sql,stid,tid);
    }
}
