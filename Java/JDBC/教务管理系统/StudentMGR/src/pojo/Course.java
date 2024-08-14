package pojo;

import java.io.Serializable;

public class Course implements Serializable {
    private Integer id;
    private String cname;
    private int type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getType() {
        return type==1?"必修":"选修";
    }

    public void setType(int type) {
        this.type = type;
    }

    public Course() {
    }

    public Course(Integer id, String cname, int type) {
        this.id = id;
        this.cname = cname;
        this.type = type;
    }

    @Override
    public String toString() {
        return "课程id:" + id +"\t"+
                ", 课程名称:'" + cname + '\'' +
                ", 类型:" + (type==1?"必修":"选修");
    }
}
