package view;

import pojo.*;
import service.ClassesService;
import service.CourseService;
import service.ScoreService;
import service.StudentService;
import service.impl.ClassesServiceImpl;
import service.impl.CourseServiceImpl;
import service.impl.ScoreServiceImpl;
import service.impl.StudentServiceImpl;
import utils.ScannerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ScoreView {
    Scanner scanner = ScannerUtils.scanner;
    ClassesService classesService = new ClassesServiceImpl();
    StudentService studentService = new StudentServiceImpl();
    CourseService courseService = new CourseServiceImpl();
    ScoreService scoreService = new ScoreServiceImpl();
    public void queryScoreView(Teacher loginteacher){
        System.out.println("*********************当前所在位置：管理员系统-->成绩管理-->查询成绩*****************");
        while (true){
            System.out.println("请选择功能: 1 全部查询 2 根据班级查询 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    showScoreView(loginteacher);
                    break;
                case 2:
                    String className = checkClassesView(loginteacher);
                    Classes classes = classesService.queryClassesByName(className);
                    Course course = courseService.queryCourseById(loginteacher.getCid());
                    List<Map<String,Object>> studentList = studentService.queryStudentByClassName(classes.getName());
                    if (studentList.size()!=0){
                        for (Map<String,Object> map:studentList){
                            Score score = scoreService.queryScoreBySidAndCid(Integer.parseInt(map.get("id").toString()),loginteacher.getCid());
                            System.out.println("班级："+className+"\t"+"姓名："+map.get("name")+"\t"+"课程："+course.getCname()+"\t"+"成绩："+score.getGrade());
                        }
                    }else {
                        System.out.println("暂无此班成绩");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void showScoreView(Teacher loginteacher){
        List<Map<String,Object>> classesList = classesService.queryClassesByTeacherId(loginteacher.getId());
        Course course = courseService.queryCourseById(loginteacher.getCid());
        for (Map<String,Object> map:classesList){
            List<Map<String,Object>> studentList = studentService.queryStudentByClassName(map.get("name").toString());
            for (Map<String,Object> map1:studentList){
                Score score = scoreService.queryScoreBySidAndCid(Integer.parseInt(map1.get("id").toString()),loginteacher.getCid());
                System.out.println("班级："+map.get("name")+"\t"+"姓名："+map1.get("name")+"\t"+"课程："+course.getCname()+"\t"+"成绩："+score.getGrade());
            }
        }
    }
    public String checkClassesView(Teacher loginteacher){
        while (true){
            System.out.println("请输入学生所在班级名称");
            String className = scanner.next();
            ArrayList<String> classesNameList = new ArrayList<>();
            List<Map<String,Object>> classesList = classesService.queryClassesByTeacherId(loginteacher.getId());
            for (Map<String,Object> map:classesList){
                classesNameList.add(map.get("name").toString());
            }
            if (classesNameList.contains(className)){
                return className;
            }else {
                System.out.println("你不授课此班级,请重新输入");
            }
        }
    }
    public int checkStudent(String classesName){
        List<Map<String,Object>> studentList = studentService.queryStudentByClassName(classesName);
        while (true){
            System.out.println("请输入学生编号");
            int sid = scanner.nextInt();
            ArrayList<Integer> studentIdList = new ArrayList<>();
            for (Map<String,Object> map:studentList){
                studentIdList.add(Integer.parseInt(map.get("id").toString()));
            }
            if (studentIdList.contains(sid)){
                return sid;
            }else {
                System.out.println("该班级没有该学生，请重新输入");
            }
        }

    }
    public void addScoreView(Teacher loginteacher){
        System.out.println("*********************当前所在位置：管理员系统-->成绩管理-->添加学生成绩*****************");
        while (true){
            System.out.println("请选择功能: 1 添加学生成绩 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    String className = checkClassesView(loginteacher);
                    int sid = checkStudent(className);
                    if (scoreService.queryScoreBySidAndCid(sid,loginteacher.getCid()) == null){
                        System.out.println("请输入学生成绩");
                        double grade = scanner.nextDouble();
                        if (scoreService.addScoreBySidAndCid(sid,loginteacher.getCid(),grade)){
                            System.out.println("添加成功");
                        }else {
                            System.out.println("添加失败");
                        }
                        break;
                    }else {
                        System.out.println("该学生已存在成绩");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void modifyScoreView(Teacher loginteacher){
        System.out.println("*********************当前所在位置：管理员系统-->成绩管理-->修改学生成绩*****************");
        while (true){
            System.out.println("请选择功能: 1 修改学生成绩 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    String className = checkClassesView(loginteacher);
                    int sid = checkStudent(className);
                    if (scoreService.queryScoreBySidAndCid(sid,loginteacher.getCid()) != null){
                        System.out.println("请输入学生新成绩");
                        double grade = scanner.nextDouble();
                        if (scoreService.editScoreBySidAndCid(sid,loginteacher.getCid(),grade)){
                            System.out.println("修改成功");
                        }else {
                            System.out.println("修改失败");
                        }
                        break;
                    }else {
                        System.out.println("该学生不存在成绩");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
}
