package dao.impl;

import dao.ClassesDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Classes;
import utils.JdbcUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClassesDaoImpl implements ClassesDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertClasses(Classes classes) {
        String sql = "insert into classes(name) values(?)";
        int i = jdbcTemplate.update(sql,classes.getName());
        return i;
    }

    @Override
    public int DeleteClassesById(int id) {
        String sql = "delete from classes where id = ?";
        int i = jdbcTemplate.update(sql,id);
        return i;
    }

    @Override
    public int DeleteClassesByName(String name) {
        String sql = "delete from classes where name = ?";
        return jdbcTemplate.update(sql,name);
    }

    @Override
    public int UpdateClasses(Classes classes) {
        String sql = "update classes set name = ? where id = ?";
        int i = jdbcTemplate.update(sql,classes.getName(),classes.getId());
        return i;
    }

    @Override
    public Classes SelectClassesById(int id) {
        String sql = "select * from classes where id = ?";
        List<Classes> classesList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Classes>(Classes.class),id);
        if (classesList.isEmpty()){
            return null;
        }
        return classesList.get(0);
    }

    @Override
    public List<Classes> SelectAllClasses() {
        String sql = "select * from classes";
        List<Classes> classesList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Classes>(Classes.class));
        return classesList;
    }

    @Override
    public Classes SelectClassesByName(String name) {
        String sql = "select * from classes where name = ?";
        List<Classes> classesList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Classes>(Classes.class),name);
        if (classesList.isEmpty()) {
            return null;
        } else {
            return classesList.get(0);
        }
    }

    @Override
    public List<Map<String, Object>> SelectClassesByTeacherId(int tid) {
        String sql = "select c.* from teacher t,classes c,teachingclass tc where t.id = tc.tid and c.id = tc.classid and t.id = ?";
        return jdbcTemplate.queryForList(sql,tid);
    }

    @Override
    public int UpdateClassesName(String name, String newName) {
        String sql = "update classes set name = ? where name = ?";
        return jdbcTemplate.update(sql,newName,name);
    }

    @Override
    public int InsertClassesTeacher(int tid, int cid) {
        String sql = "insert into teachingclass values (null,?,?)";
        return jdbcTemplate.update(sql,tid,cid);
    }

    @Override
    public int DeleteClassesTeacherByTid(int tid) {
        String sql = "delete from teachingclass where tid=?";
        return jdbcTemplate.update(sql,tid);
    }
}
