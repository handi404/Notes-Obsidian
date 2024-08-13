package pojo;

import java.io.Serializable;

public class Classes implements Serializable {
    private Integer id;
    private String name;

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

    public Classes(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Classes() {
    }

    @Override
    public String toString() {
        return "班级编号:" + id +" "+"班级名称:" + name;
    }
}
