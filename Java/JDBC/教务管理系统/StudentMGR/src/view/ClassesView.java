package view;

import pojo.Classes;
import pojo.Course;
import pojo.Teacher;
import service.ClassesService;
import service.CourseService;
import service.TeacherService;
import service.impl.ClassesServiceImpl;
import service.impl.CourseServiceImpl;
import service.impl.TeacherServiceImpl;
import utils.ScannerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClassesView {
    Scanner scanner = ScannerUtils.scanner;
    ClassesService classesService = new ClassesServiceImpl();
    TeacherService teacherService = new TeacherServiceImpl();
    CourseService courseService = new CourseServiceImpl();
    public void queryClassesView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>班级信息查询**********");
            System.out.println("请选择功能:1 查询全部班级  2 根据名称查询  0 返回上级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    List<Classes> classesList = classesService.queryAllClasses();
                    showClasses(classesList);
                    break;
                case 2:
                    System.out.println("请输入班级名称：");
                    String name = scanner.next();
                    Classes classes = classesService.queryClassesByName(name);
                    if (classes == null){
                        System.out.println("未查询到该班级");
                    }else {
                        System.out.println(classes);
                        System.out.println("任课教师:");
                        List<Map<String,Object>> teacherList = teacherService.queryTeacherByClassName(name);
                        for (Map<String,Object> map:teacherList){
                            int id = (int)map.get("id");
                            Course course = courseService.queryCourseById(id);
                            System.out.println("姓名:"+map.get("name")+"\t"+"课程:"+course.getCname());
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void showClasses(List<Classes> classesList){
        if (classesList.size() == 0){
            System.out.println("暂无班级信息");
            return;
        }
        System.out.println("**********************查询结果如下：*******************************");
        for (Classes classes:classesList){
            System.out.println(classes);
            System.out.println("任课教师:");
            String className = classes.getName();
            List<Map<String,Object>> teacherList = teacherService.queryTeacherByClassName(className);
            for (Map<String,Object> map:teacherList){
                int id = (int)map.get("id");
                Course course = courseService.queryCourseById(id);
                System.out.println("姓名:"+map.get("name")+"\t"+"课程:"+course.getCname());
            }
        }
    }
    public void addClassesView(){
        Classes classes = new Classes();
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>添加班级**********");
            System.out.println("请输入班级名称:");
            String name = scanner.next();
            boolean bl = classesService.checkClassName(name);
            if (!bl){
                classes.setName(name);
                classesService.addClasses(classes);
                System.out.println("添加成功,是否继续添加y/n?");
                if (!out()){
                    return;
                }
            }
            System.out.println("该班级已经存在，请重新添加");
        }
    }
    public Boolean out(){
        while (true){
            String opt = scanner.next();
            if ("n".equalsIgnoreCase(opt)) {
                return false;
            } else if ("y".equalsIgnoreCase(opt)) {
                return true;
            } else {
                System.out.println("输入有误，请重新输入：y/n");
            }
        }
    }
    public void editClassesView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>修改班级**********");
            System.out.println("*******************班级信息如下：**********************");
            List<Classes> classesList = classesService.queryAllClasses();
            showClasses(classesList);
            System.out.println("*****************************************************");
            System.out.println("请选择功能:1 修改班级名称 2 修改班级任课教师 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入修改的班级名称：");
                    String name;
                    while (true){
                        name = scanner.next();
                        boolean bl = classesService.checkClassName(name);
                        if (bl){
                            break;
                        }
                        System.out.println("该班级不存在，请重新输入");
                    }
                    System.out.println("请输入新班级名称：");
                    while (true){
                        String newName = scanner.next();
                        boolean bl = classesService.checkClassName(newName);
                        if (!bl){
                            classesService.editClassesName(name,newName);
                            System.out.println("修改成功");
                            break;
                        }
                        System.out.println("该班级已存在，请重新输入");
                    }
                case 2:
                    while (true){
                        System.out.println("**********当前所在位置:管理员系统>班级管理>班级教师信息修改**********");
                        System.out.println("请选择功能：1 新增班级教师 2 移除班级教师 0 返回上一级");
                        int num1 = scanner.nextInt();
                        switch (num1){
                            case 1:
                                addClassTeacherView();
                                break;
                            case 2:
                                removeClassTeacherView();
                                break;
                            case 0:
                                return;
                            default:
                                System.out.println("输入错误，请重新输入");
                        }
                    }
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void addClassTeacherView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>修改班级>新增班级教师**********");
            System.out.println("请输入班级名称：");
            String className = scanner.next();
            boolean bl = classesService.checkClassName(className);
            if (bl){
                while (true){
                    System.out.println("请输入教师编号：");
                    int tid = scanner.nextInt();
                    boolean bl1 = teacherService.findTeacherById(tid);
                    if (bl1){
                        boolean bl2 = classesService.addClassTeacher(tid,classesService.queryClassesByName(className).getId());
                        if (bl2){
                            System.out.println("添加成功，是否继续？y/n");
                            if (!out()){
                                return;
                            }
                        }else {
                            System.out.println("课程教师已经存在，请重新输入");
                        }
                    }else {
                        System.out.println("该教师不存在，请重新输入");
                    }
                }
            }else {
                System.out.println("该班级不存在，请重新输入");
            }
        }
    }
    public void removeClassTeacherView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>修改班级>移除班级教师**********");
            System.out.println("请输入班级名称：");
            String className = scanner.next();
            boolean bl = classesService.checkClassName(className);
            if (bl){
                List<Map<String,Object>> teacherList = teacherService.queryTeacherByClassName(className);
                if (teacherList.size() == 0){
                    System.out.println("该班级没有教师，请重新输入");
                }else {
                    System.out.println("该班级教师信息如下：");
                    ArrayList<Integer> tids = new ArrayList<>();
                    for (Map<String,Object> map : teacherList){
                        Course course = courseService.queryCourseById((int)map.get("cid"));
                        System.out.println("教师编号：" + map.get("tid") + "\t教师姓名：" + map.get("name")+ "\t教授课程："+course.getCname());
                        tids.add((int)map.get("tid"));
                    }
                    System.out.println("请输入要移除的教师编号：");
                    while (true){
                        int tid = scanner.nextInt();
                        if (tids.contains(tid)){
                            classesService.removeClassesTeacherByTid(tid);
                            System.out.println("移除成功，是否继续？y/n");
                            if (!out()){
                                return;
                            }
                            break;
                        }else {
                            System.out.println("教师编号有误，请重新输入：");
                        }
                    }
                }
            }else {
                System.out.println("该班级不存在，请重新输入");
            }
        }
    }
    public void removeClassesView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>班级信息管理>移除班级**********");
            System.out.println("请输入要移除班级名称：");
            String className = scanner.next();
            boolean bl = classesService.checkClassName(className);
            if (bl){
                classesService.removeClassesByName(className);
                System.out.println("删除成功，是否继续删除？y/n");
                if (!out()) {
                    return;
                }
            }else {
                System.out.println("该班级不存在，请重新输入");
            }
        }
    }
}
