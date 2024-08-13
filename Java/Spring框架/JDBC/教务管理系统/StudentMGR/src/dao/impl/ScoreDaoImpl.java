package dao.impl;

import dao.ScoreDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pojo.Score;
import utils.JdbcUtils;

import java.util.Collections;
import java.util.List;

public class ScoreDaoImpl implements ScoreDao {
    private JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate();
    @Override
    public int InsertScore(Score score) {
        String sql = "insert into score(scid,grade,cid,sid) values(?,?,?,?)";
        int i = jdbcTemplate.update(sql,score.getScid(),score.getGrade(),score.getCid(),score.getSid());
        return i;
    }

    @Override
    public int DeleteScoreById(int id) {
        String sql = "delete from score where scid = ?";
        int i = jdbcTemplate.update(sql,id);
        return i;
    }

    @Override
    public int UpdateScore(Score score) {
        String sql = "update score set grade = ?,cid = ?,sid = ? where scid = ?";
        int i = jdbcTemplate.update(sql,score.getGrade(),score.getCid(),score.getSid(),score.getScid());
        return i;
    }

    @Override
    public Score SelectScoreById(int id) {
        String sql = "select * from score where scid = ?";
        List<Score> scoreList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Score>(Score.class),id);
        if (scoreList.isEmpty()){
            return null;
        }
        return scoreList.get(0);
    }

    @Override
    public List<Score> SelectAllScore() {
        String sql = "select * from score";
        List<Score> scoreList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Score>(Score.class));
        return scoreList;
    }

    @Override
    public Score SelectScoreBySidAndCid(int sid, int cid) {
        String sql = "select * from score where sid = ? and cid = ?";
        List<Score> scoreList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Score>(Score.class),sid,cid);
        if (scoreList.isEmpty()){
            return null;
        }
        return scoreList.get(0);
    }

    @Override
    public List<Score> SelectScoreBySid(int sid) {
        String sql = "select * from score where sid = ?";
        List<Score> scoreList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Score>(Score.class),sid);
        if (scoreList.isEmpty()){
            return null;
        }
        return scoreList;
    }

    @Override
    public int InsertScoreBySidAndCid(int sid, int cid, double grade) {
        String sql = "insert into score(grade,cid,sid) values(?,?,?)";
        return jdbcTemplate.update(sql,grade,cid,sid);
    }

    @Override
    public int UpdateScoreBySidAndCid(int sid, int cid, double grade) {
        String sql = "update score set grade = ? where sid = ? and cid = ?";
        return jdbcTemplate.update(sql,grade,sid,cid);
    }
}
