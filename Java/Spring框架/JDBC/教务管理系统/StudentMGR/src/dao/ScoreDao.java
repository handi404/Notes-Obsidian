package dao;

import pojo.Score;

import java.util.List;

public interface ScoreDao {
    public int InsertScore(Score score);
    public int DeleteScoreById(int id);
    public int UpdateScore(Score score);
    public Score SelectScoreById(int id);
    public List<Score> SelectAllScore();
    public Score SelectScoreBySidAndCid(int sid,int cid);
    public List<Score> SelectScoreBySid(int sid);
    public int InsertScoreBySidAndCid(int sid,int cid,double grade);
    public int UpdateScoreBySidAndCid(int sid,int cid,double grade);
}
