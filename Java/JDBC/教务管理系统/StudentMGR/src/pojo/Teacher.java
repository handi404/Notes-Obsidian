package pojo;

import utils.DataUtil;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;

public class Teacher implements Serializable {
    private Integer id;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String password;
    private Date joinedDate;
    private int cid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Teacher(Integer id, String name, String gender, String phone, String address, String password, Date joinedDate, int cid) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.joinedDate = joinedDate;
        this.cid = cid;
    }

    public Teacher() {
    }

    @Override
    public String toString() {
        return "教师编号:" + id +
                ", 姓名:'" + name + '\'' +
                ", 性别:'" + gender + '\'' +
                ", 联系电话:'" + phone + '\'' +
                ", 地址:'" + address + '\'' +
                ", 入职日期:" + DataUtil.format(joinedDate,"yyyy-MM-dd");
    }
}
