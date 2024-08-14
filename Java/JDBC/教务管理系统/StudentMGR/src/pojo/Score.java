package pojo;

import java.io.Serializable;

public class Score implements Serializable {
    private Integer scid;
    private double grade;
    private int cid;
    private String sid;

    public Integer getScid() {
        return scid;
    }

    public void setScid(Integer scid) {
        this.scid = scid;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Score() {
    }

    public Score(Integer scid, double grade, int cid, String sid) {
        this.scid = scid;
        this.grade = grade;
        this.cid = cid;
        this.sid = sid;
    }

    @Override
    public String toString() {
        return  "scid=" + scid +
                ",成绩：" + grade +
                ", 课程编号：" + cid +
                ", 学生编号：" + sid;
    }
}
