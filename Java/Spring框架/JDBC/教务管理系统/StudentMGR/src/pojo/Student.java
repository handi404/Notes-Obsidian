package pojo;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private Integer id;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String password;
    private Date enrollmentTime;
    private int classId;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public int getClassId() {
        return classId;
    }

    public Date getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnrollmentTime(Date enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public Student() {
    }

    public Student(Integer id, String name, String gender, String phone, String address, String password, Date enrollmentTime, int classId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.enrollmentTime = enrollmentTime;
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", enrollmentTime='" + enrollmentTime + '\'' +
                ", classId=" + classId +
                '}';
    }
}
