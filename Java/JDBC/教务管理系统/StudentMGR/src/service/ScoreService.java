package service;

import pojo.Score;

import java.util.List;

public interface ScoreService {
    public Score queryScoreBySidAndCid(int sid, int cid);
    public List<Score> queryScoreBySid(int sid);
    public boolean addScoreBySidAndCid(int sid, int cid, double grade);
    public boolean editScoreBySidAndCid(int sid, int cid, double grade);
}
