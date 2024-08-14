package service.impl;

import dao.ClassesDao;
import dao.impl.ClassesDaoImpl;
import pojo.Classes;
import service.ClassesService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClassesServiceImpl implements ClassesService {
    ClassesDao classesDao = new ClassesDaoImpl();
    @Override
    public List<Classes> queryAllClasses() {
        return classesDao.SelectAllClasses();
    }

    @Override
    public Classes queryClassesByName(String name) {
        if (classesDao.SelectClassesByName(name) != null){
            return classesDao.SelectClassesByName(name);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryClassesByTeacherId(int id) {
        return classesDao.SelectClassesByTeacherId(id);
    }

    @Override
    public boolean checkClassName(String name) {
        if (classesDao.SelectClassesByName(name) != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addClasses(Classes classes) {
        if (classesDao.InsertClasses(classes) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean editClassesName(String name, String newName) {
        if (classesDao.UpdateClassesName(name,newName) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addClassTeacher(int tid, int cid) {
        if (classesDao.InsertClassesTeacher(tid,cid) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean removeClassesTeacherByTid(int tid) {
        if (classesDao.DeleteClassesTeacherByTid(tid) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean removeClassesByName(String name) {
        if (classesDao.DeleteClassesByName(name) > 0){
            return true;
        }else {
            return false;
        }
    }
}
