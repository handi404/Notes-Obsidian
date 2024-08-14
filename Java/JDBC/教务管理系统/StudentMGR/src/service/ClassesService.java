package service;

import pojo.Classes;
import pojo.Course;

import java.util.List;
import java.util.Map;

public interface ClassesService {
    public List<Classes> queryAllClasses();
    public Classes queryClassesByName(String name);
    public List<Map<String,Object>> queryClassesByTeacherId(int id);
    public boolean checkClassName(String name);
    public boolean addClasses(Classes classes);
    public boolean editClassesName(String name,String newName);
    public boolean addClassTeacher(int tid,int cid);
    public boolean removeClassesTeacherByTid(int tid);
    public boolean removeClassesByName(String name);
}
