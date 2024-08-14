package service.impl;

import dao.ScoreDao;
import dao.impl.ScoreDaoImpl;
import pojo.Score;
import service.ScoreService;

import java.util.Collections;
import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    ScoreDao scoreDao = new ScoreDaoImpl();
    @Override
    public Score queryScoreBySidAndCid(int sid, int cid) {
        return scoreDao.SelectScoreBySidAndCid(sid,cid);
    }

    @Override
    public List<Score> queryScoreBySid(int sid) {
        return scoreDao.SelectScoreBySid(sid);
    }

    @Override
    public boolean addScoreBySidAndCid(int sid, int cid, double grade) {
        if (scoreDao.InsertScoreBySidAndCid(sid,cid,grade) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editScoreBySidAndCid(int sid, int cid, double grade) {
        if (scoreDao.UpdateScoreBySidAndCid(sid,cid,grade) > 0){
            return true;
        }
        return false;
    }
}
