package dao.impl;

import dao.StudentDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Student;
import utils.JdbcUtils;

import java.util.*;

public class StudentDaoImpl implements StudentDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertStudent(Student student) {
        String sql = "insert into student(name,gender,phone,address,password,enrollmentTime,classId) values(?,?,?,?,?,?,?)";
        int i = jdbcTemplate.update(sql,student.getName(),student.getGender(),student.getPhone(),student.getAddress(),student.getPassword(),student.getEnrollmentTime(),student.getClassId());
        return i;
    }

    @Override
    public int DeleteStudentById(int id) {
        String sql = "delete from student where id = ?";
        int i = jdbcTemplate.update(sql,id);
        return i;
    }

    @Override
    public int UpdateStudent(Student student) {
        String sql = "update student set name = ?,gender = ?,phone = ?,address = ?,password = ?,enrollmentTime = ?,classId = ? where id = ?";
        int i = jdbcTemplate.update(sql,student.getName(),student.getGender(),student.getPhone(),student.getAddress(),student.getPassword(),student.getEnrollmentTime(),student.getClassId(),student.getId());
        return i;
    }

    @Override
    public Student SelectStudentById(int id) {
        String sql = "select * from student where id = ?";
        List<Student> studentList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Student>(Student.class),id);
        if (studentList.isEmpty()) {
            return null;
        }
        return studentList.get(0);
    }

    @Override
    public List<Student> SelectAllStudent() {
        String sql = "select * from student";
        List<Student> studentList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Student>(Student.class));
        return studentList;
    }

    @Override
    public List<Student> SelectStudentByName(String name) {
        String sql = "select * from student where name = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Student>(Student.class),name);
    }

    @Override
    public List<Map<String,Object>> SelectStudentByClassName(String className) {
        String sql = "select s.* from student s,classes c where s.classid = c.id and c.name = ?";
        return jdbcTemplate.queryForList(sql,className);
    }

    @Override
    public List<Map<String,Object>> SelectStudentByJoinTime(Date time) {
        String sql = "select * from student where enrollmentTime = ?";
        return jdbcTemplate.queryForList(sql,time);
    }

    @Override
    public List<Student> SelectStudentByPhone(String phone) {
        String sql = "select * from student where phone = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Student>(Student.class),phone);
    }

    @Override
    public Student findStudentByUsernameAndPassword(Object[] userInfo) {
        String sql = "select * from student where phone = ? and password = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Student>(Student.class),userInfo);
    }

    @Override
    public int updateNameById(int id, String name) {
        String sql = "update student set name = ? where id = ?";
        return jdbcTemplate.update(sql,name,id);
    }

    @Override
    public int updateGenderById(int id, String gender) {
        String sql = "update student set gender = ? where id = ?";
        return jdbcTemplate.update(sql,gender,id);
    }

    @Override
    public int updatePasswordById(int id, String password) {
        String sql = "update student set password = ? where id = ?";
        return jdbcTemplate.update(sql,password,id);
    }

    @Override
    public int updateAddressById(int id, String address) {
        String sql = "update student set address = ? where id = ?";
        return jdbcTemplate.update(sql,address,id);
    }

    @Override
    public int updatePhoneById(int id, String Phone) {
        String sql = "update student set phone = ? where id = ?";
        return jdbcTemplate.update(sql,Phone,id);
    }

    @Override
    public int updateClassIdById(int id, int classId) {
        String sql = "update student set classid = ? where id = ?";
        return jdbcTemplate.update(sql,classId,id);
    }
}
