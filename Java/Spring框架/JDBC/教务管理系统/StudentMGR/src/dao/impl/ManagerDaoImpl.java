package dao.impl;

import dao.ManagerDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Manager;
import utils.JdbcUtils;

import java.util.Collections;
import java.util.List;

public class ManagerDaoImpl implements ManagerDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertManager(Manager manager) {
        String sql = "insert into manager(username,password) values(?,?)";
        int i = jdbcTemplate.update(sql,manager.getUsername(),manager.getPassword());
        return i;
    }

    @Override
    public int DeleteManagerById(int id) {
        String sql = "delete from manager where id = ?";
        int i = jdbcTemplate.update(sql,id);
        return i;
    }

    @Override
    public int UpdateManager(Manager manager) {
        String sql = "update manager set username = ?,password = ? where id = ?";
        int i = jdbcTemplate.update(sql,manager.getUsername(),manager.getPassword(),manager.getId());
        return i;
    }

    @Override
    public Manager SelectManagerById(int id) {
        String sql = "select * from manager where id = ?";
        List<Manager> managerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Manager>(Manager.class), id);
        if (managerList.isEmpty()){
            return null;
        }
        return managerList.get(0);
    }

    @Override
    public Manager SelectManagerByIdAndPassword(int id, String password) {
        String sql = "select * from manager where id = ? and password = ?";
        List<Manager> managerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Manager>(Manager.class), id, password);
        if (managerList.isEmpty()){
            return null;
        }else {
            return managerList.get(0);
        }
    }

    @Override
    public List<Manager> SelectAllManager() {
        String sql = "select * from manager";
        List<Manager> managerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Manager>(Manager.class));
        return managerList;
    }
    @Override
    public Manager findManagerByUsernameAndPassword(Object[] userInfo) {
        String sql = "select * from manager where username = ? and password = ?";

        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Manager>(Manager.class), userInfo);
    }

    @Override
    public int UpdatePasswordByIdAndPassword(int id, String password, String newPassword) {
        String sql = "update manager set password = ? where id = ? and password = ?";
        return jdbcTemplate.update(sql,newPassword,id,password);
    }
}
