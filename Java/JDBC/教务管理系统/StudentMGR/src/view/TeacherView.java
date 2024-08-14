package view;

import pojo.Course;
import pojo.Teacher;
import service.CourseService;
import service.TeacherService;
import service.impl.CourseServiceImpl;
import service.impl.TeacherServiceImpl;
import utils.ScannerUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TeacherView {
    Scanner scanner = ScannerUtils.scanner;
    TeacherService teacherService = new TeacherServiceImpl();
    public void addTeacherView(){
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->教师信息管理-->添加教师信息*****************");
            Teacher teacher = new Teacher();
            System.out.println("请输入教师姓名:");
            String name = scanner.next();
            teacher.setName(name);
            System.out.println("请输入性别:男 或 女");
            String gender = checkGender();
            teacher.setGender(gender);
            System.out.println("请输入手机号码:");
            String phone = checkTeacherPhone();
            teacher.setPhone(phone);
            System.out.println("请输入家庭住址:");
            teacher.setAddress(scanner.next());
            //密码，使用手机后六位
            teacher.setPassword(teacher.getPhone().substring(5));
            //教师编号采用年+月+日+时+分+随机数
            //获取日期
            Date date = new Date();
            String dt = new SimpleDateFormat("yyyyMMddHHmm").format(date);
            //入职时间
            teacher.setJoinedDate(date);
            System.out.println("请选择所教课程编号:");
            showAllCourse();
            teacher.setCid(scanner.nextInt());
            teacher.setId(null);
            //添加
            teacherService.addTeacher(teacher);
            System.out.println("添加成功，是否继续添加：y/n");
            if (!out()){
                return;
            }
        }
    }
    public String checkGender(){
        while (true){
            String gender = scanner.next();
            if ("男".equals(gender) || "女".equals(gender)){
                return gender;
            }else {
                System.out.println("请输入正确的性别！");
            }
        }
    }
    public String checkTeacherPhone(){
        while (true) {
            String phone = scanner.next();
            boolean flag = phone.matches("^1(3[0-9]|4[5-9]|5[0-3,5-9]|6[6]|7[1-8]|8[0-9]|9[1,3,5,7,8,9])\\d{8}$");
            if (flag) {
                //查询手机号是否存在,手机号不能重复
                boolean repeated = teacherService.findTeacherByPhone(phone);
                if (!repeated) { //手机号不重复
                    return phone;
                } else {
                    System.out.println("手机号已经存在，请换个手机号码");
                }
            } else {
                System.out.println("手机号码格式不正确，请重新输入手机号码");
            }
        }
    }
    public void showAllCourse(){
        CourseService courseService = new CourseServiceImpl();
        List<Course> courseList = courseService.queryAllCourse();
        for (Course course : courseList){
            System.out.println(course);
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
    public void editTeacherView(){
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->教师信息管理-->修改教师信息*****************");
            System.out.println("请选择功能：1.修改姓名  2.修改性别 3.修改密码 4.修改电话 5.修改地址 6.修改教授课程 0.返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入教师编号");
                    int id = scanner.nextInt();
                    System.out.println("请输入教师新姓名");
                    String name = scanner.next();
                    if (teacherService.editNameById(id,name)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 2:
                    System.out.println("请输入教师编号");
                    int id1 = scanner.nextInt();
                    System.out.println("请输入教师新性别");
                    String gender = checkGender();
                    if (teacherService.editGenderById(id1,gender)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 3:
                    System.out.println("请输入教师编号");
                    int id2 = scanner.nextInt();
                    System.out.println("请输入教师新密码");
                    String password = scanner.next();
                    if (teacherService.editPasswordById(id2,password)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 4:
                    System.out.println("请输入教师编号");
                    int id3 = scanner.nextInt();
                    System.out.println("请输入教师新电话");
                    String phone = checkTeacherPhone();
                    if (teacherService.editPhoneById(id3,phone)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 5:
                    System.out.println("请输入教师编号");
                    int id4 = scanner.nextInt();
                    System.out.println("请输入教师新地址");
                    String address = scanner.next();
                    if (teacherService.editAddressById(id4,address)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 6:
                    System.out.println("请输入教师编号");
                    int id5 = scanner.nextInt();
                    System.out.println("请选择所教课程编号:");
                    showAllCourse();
                    int courseId = scanner.nextInt();
                    if (teacherService.editCidById(id5,courseId)){
                        System.out.println("修改成功！");
                    }else {
                        System.out.println("修改失败！");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("请输入正确的操作序号！");
            }
        }
    }
    public void removeTeacherView(){
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->教师信息管理-->删除教师信息*****************");
            System.out.println("*******请选择功能：1 删除教师 0 返回上一级*********");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    removeTeacher();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("请输入正确的操作序号！");
            }
        }
    }
    public void removeTeacher(){
        System.out.println("请输入要删除的教师编号:");
        int id = scanner.nextInt();
        if (teacherService.deleteTeacher(id)){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }
}

