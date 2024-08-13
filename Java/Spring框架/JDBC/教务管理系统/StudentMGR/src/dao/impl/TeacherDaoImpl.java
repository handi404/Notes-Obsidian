package dao.impl;

import dao.TeacherDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Teacher;
import utils.JdbcUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TeacherDaoImpl implements TeacherDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertTeacher(Teacher teacher) {
        String sql = "insert into teacher(name,gender,phone,address,password,joinedDate,cid) values(?,?,?,?,?,?,?)";
        int i = jdbcTemplate.update(sql,teacher.getName(),teacher.getGender(),teacher.getPhone(),teacher.getAddress(),teacher.getPassword(),teacher.getJoinedDate(),teacher.getCid());
        return i;
    }

    @Override
    public int DeleteTeacherById(int id) {
        String sql = "delete from teacher where id = ?";
        int i = jdbcTemplate.update(sql,id);
        return i;
    }

    @Override
    public int UpdateTeacher(Teacher teacher) {
        String sql = "update teacher set name = ?,gender = ?,phone = ?,address = ?,password = ?,joinedDate = ?,cid = ? where id = ?";
        int i = jdbcTemplate.update(sql,teacher.getName(),teacher.getGender(),teacher.getPhone(),teacher.getAddress(),teacher.getPassword(),teacher.getJoinedDate(),teacher.getCid(),teacher.getId());
        return i;
    }

    @Override
    public Teacher SelectTeacherById(int id) {
        String sql = "select id, name, gender, phone, address, password, joineddate, cid from teacher where id = ?";
        List<Teacher> teacherList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Teacher>(Teacher.class),id);
        if (teacherList.isEmpty()){
            return null;
        }
        return teacherList.get(0);
    }

    @Override
    public List<Teacher> SelectAllTeacher() {
        String sql = "select id, name, gender, phone, address, password, joineddate, cid from teacher";
        List<Teacher> teacherList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Teacher>(Teacher.class));
        if (teacherList.isEmpty()){
            return null;
        }
        return teacherList;
    }
    @Override
    public Teacher SelectTeacherByUsernameAndPassword(Object[] userInfo) {
        String sql = "select id, name, gender, phone, address, password, joineddate, cid from teacher where phone = ? and password = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Teacher>(Teacher.class),userInfo);
    }
    @Override
    public List<Teacher> SelectTeacherByName(String name) {
        String sql = "select id, name, gender, phone, address, password, joineddate, cid from teacher where name = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Teacher>(Teacher.class),name);
    }
    @Override
    public List<Map<String, Object>> SelectTeacherByClassName(String className){
        String sql = "select t.*,c.name as 'className' from teacher t,classes c,teachingclass tc where t.id = tc.tid and c.id = tc.classid and c.name = ?";
        return jdbcTemplate.queryForList(sql,className);
    }
    @Override
    public List<Map<String,Object>> SelectTeacherByCourseName(String courseName){
        String sql = "select t.*,c.cname as 'courseName' from teacher t,course c where t.cid = c.id and c.cname = ?";
        return jdbcTemplate.queryForList(sql,courseName);
    }

    @Override
    public Teacher SelectTeacherByClassIdAndCourseId(int classId, int courseId) {
        String sql = "select t.* from teacher t,teachingclass tc,course c,classes cl where cl.id = tc.classid and tc.tid = t.id and c.id = t.id and cl.id = ? and c.id = ?";
        List<Teacher> teacherList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Teacher>(Teacher.class),classId,courseId);
        if (teacherList.isEmpty()){
            return null;
        }
        return teacherList.get(0);
    }

    public List<Teacher> SelectTeacherByPhone(String phone){
        String sql = "select id, name, gender, phone, address, password, joineddate, cid from teacher where phone = ?";
        if (jdbcTemplate.queryForList(sql,phone).isEmpty()){
            return null;
        }
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Teacher.class),phone);
    }
    public int updateNameById(int id, String name){
        String sql = "update teacher set name = ? where id = ?";
        return jdbcTemplate.update(sql,name,id);
    }

    @Override
    public int updateGenderById(int id, String gender) {
        String sql = "update teacher set gender = ? where id = ?";
        return jdbcTemplate.update(sql,gender,id);
    }

    @Override
    public int updatePasswordById(int id, String password) {
        String sql = "update teacher set password = ? where id = ?";
        return jdbcTemplate.update(sql,password,id);
    }

    @Override
    public int updateAddressById(int id, String address) {
        String sql = "update teacher set address = ? where id = ?";
        return jdbcTemplate.update(sql,address,id);
    }

    @Override
    public int updateCidById(int id, int cid) {
        String sql = "update teacher set cid = ? where id = ?";
        return jdbcTemplate.update(sql,cid,id);
    }

    @Override
    public int updatePhoneById(int id, String Phone) {
        String sql = "update teacher set phone = ? where id = ?";
        return jdbcTemplate.update(sql,Phone,id);
    }
}
