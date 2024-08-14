package dao;

import pojo.Classes;

import java.util.List;
import java.util.Map;

public interface ClassesDao {
    public int InsertClasses(Classes classes);
    public int DeleteClassesById(int id);
    public int DeleteClassesByName(String name);
    public int UpdateClasses(Classes classes);
    public Classes SelectClassesById(int id);
    public List<Classes> SelectAllClasses();
    public Classes SelectClassesByName(String name);
    public List<Map<String,Object>> SelectClassesByTeacherId(int tid);
    public int UpdateClassesName(String name,String newName);
    public int InsertClassesTeacher(int tid,int cid);
    public int DeleteClassesTeacherByTid(int tid);
}
