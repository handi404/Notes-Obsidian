package view;

import pojo.Course;
import service.CourseService;
import service.impl.CourseServiceImpl;
import utils.ScannerUtils;

import java.util.List;
import java.util.Scanner;

public class CourseView {
    Scanner scanner = ScannerUtils.scanner;
    CourseService courseService = new CourseServiceImpl();
    public void queryCourseView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>课程信息管理>课程信息查询**********");
            System.out.println("请选择功能:1 查询全部课程  2 根据类型查询  0 返回上级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    List<Course> courseList = courseService.queryAllCourse();
                    showCourseInfo(courseList);
                    break;
                case 2:
                    System.out.println("请选择类型：1 必修 2 选秀");
                    String type = chooseType();
                    List<Course> courseList1 = courseService.queryCourseByType(type);
                    showCourseInfo(courseList1);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void showCourseInfo(List<Course> courseList){
        System.out.println("**********************查询结果如下：*******************************");
        for (Course course:courseList){
            System.out.println(course);
        }
    }
    public String chooseType(){
        while (true){
            String choose = scanner.next();
            if ("1".equals(choose) || "2".equals(choose)){
                return choose;
            }else {
                System.out.println("暂无该类型，请重新选择");
            }
        }
    }
    public void addCourseView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>课程信息管理>添加课程信息**********");
            Course course = new Course();
            System.out.println("请输入课程名称:");
            String cname = scanner.next();
            boolean bl = courseService.CheckCname(cname);
            if (!bl){
                course.setCname(cname);
                System.out.println("请选择课程类型：1 必修 2 选修");
                String tid = chooseType();
                course.setType(Integer.valueOf(tid));
                System.out.println("添加完成是否继续？y/n");
                if (!out()){
                    break;
                }
            }
            System.out.println("该课程已经存在，请重新添加");
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
    public void editCourseView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>课程信息管理>课程信息修改**********");
            System.out.println("*******************当前课程信息如下：**********************");
            List<Course> courseList = courseService.queryAllCourse();
            if (courseList.size() == 0){
                System.out.println("暂无课程信息，请先添加课程。");
            }
            showCourseInfo(courseList);
            System.out.println("请选择功能:1 修改课程名 2 修改课程类型 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入修改的课程名称：");
                    String name;
                    while (true){
                        name = scanner.next();
                        boolean bl = courseService.CheckCname(name);
                        if(bl){
                            break;
                        }
                        System.out.println("该课程不存在，请重新输入");
                    }
                    System.out.println("请输入新课程名称：");
                    while (true){
                        String newName = scanner.next();
                        boolean bl = courseService.CheckCname(newName);
                        if (!bl){
                            courseService.editName(name,newName);
                            System.out.println("修改成功");
                            break;
                        }
                        System.out.println("该课程已存在");
                    }
                case 2:
                    System.out.println("请输入修改的课程名称：");
                    String name1;
                    while (true){
                        name1 = scanner.next();
                        boolean bl = courseService.CheckCname(name1);
                        if(bl){
                            break;
                        }
                        System.out.println("该课程不存在，请重新输入");
                    }
                    System.out.println("请选择类型：");
                    String type = chooseType();
                    courseService.editTypeByName(name1,type);
                    System.out.println("修改成功");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void removeCourseView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>课程信息管理>删除课程信息**********");
            System.out.println("*******************当前课程信息如下：**********************");
            List<Course> courseList = courseService.queryAllCourse();
            if (courseList.size() == 0){
                System.out.println("暂无课程信息，请先添加课程。");
            }
            showCourseInfo(courseList);
            System.out.println("请选择功能:1 删除课程 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    removeCourse();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }
        }
    }
    public void removeCourse(){
        System.out.println("请输入要删除课程名称:");
        String name = scanner.next();
        boolean bl = courseService.CheckCname(name);
        if (bl){
            courseService.removeCourseByName(name);
            System.out.println("删除成功");
        }else {
            System.out.println("该课程不存在，请重新输入");
        }
    }
}
