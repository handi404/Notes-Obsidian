package view;

import dao.ClassesDao;
import pojo.Classes;
import pojo.Student;
import pojo.Student;
import service.ClassesService;
import service.StudentService;
import service.impl.ClassesServiceImpl;
import service.impl.StudentServiceImpl;
import utils.DataUtil;
import utils.ScannerUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentView {
    Scanner scanner = ScannerUtils.scanner;
    StudentService studentService = new StudentServiceImpl();
    public void queryStudentView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>学生信息管理>学生信息查询**********");
            System.out.println("请选择功能:1 全部查询 2 根据姓名查询 3 根据班级查询 4 根据入学时间查询 0 返回上一级");
            switch (scanner.nextInt()){
                case 1:
                    List<Student> studentList = studentService.queryAllStudent();
                    showStudentInfo(studentList);
                    break;
                case 2:
                    showStudentByName();
                    break;
                case 3:
                    showStudentAndClassName();
                    break;
                case 4:
                    showStudentAndJoinTime();
                    break;
                case 0:
                    return;
                default:
                   System.out.println("输入错误，请重新输入");
               }
        }
    }
    public void showStudentInfo(List<Student> studentList){
        System.out.println("**********************查询结果如下：*******************************");
        for (Student student: studentList){
            System.out.println(student);
        }
    }
    public void showStudentByName(){
        System.out.println("********************请输入学生姓名*********************");
        String name = scanner.next();
        List<Student> studentList = studentService.queryStudentByName(name);
        showStudentInfo(studentList);
    }
    public void showStudentAndClassName(){
        System.out.println("********************请输入班级名称*********************");
        String name = scanner.next();
        List<Map<String,Object>> studentList = studentService.queryStudentByClassName(name);
        for (Map<String,Object> map:studentList){
            System.out.println(map);
        }
    }
    public void showStudentAndJoinTime(){
        System.out.println("********************请输入入学时间*********************");
        Date time = DataUtil.parse(scanner.next(),"yyyy-MM-dd");
        List<Map<String,Object>> studentList = studentService.queryStudentByJoinTime(time);
        for (Map<String,Object> map:studentList){
            System.out.println(map);
        }
    }
    public void addStudentView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>学生信息管理>添加学生信息**********");
            Student student = new Student();
            System.out.println("请输入学生姓名：");
            student.setName(scanner.next());
            System.out.println("请输入性别:男 或 女");
            String gender = checkGender();
            student.setGender(gender);
            System.out.println("请输入手机号码:");
            String phone = checkStudentPhone();
            student.setPhone(phone);
            System.out.println("请输入家庭住址:");
            student.setAddress(scanner.next());
            //密码，使用手机后六位
            student.setPassword(student.getPhone().substring(5));
            //获取日期
            Date date = new Date();
            String dt = new SimpleDateFormat("yyyyMMddHHmm").format(date);
            //入校时间
            student.setEnrollmentTime(date);
            System.out.println("请选择班级");
            showAllClasses();
            student.setClassId(scanner.nextInt());
            studentService.addStudent(student);
            System.out.println("添加成功");
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
    public String checkStudentPhone(){
        while (true) {
            String phone = scanner.next();
            boolean flag = phone.matches("^1(3[0-9]|4[5-9]|5[0-3,5-9]|6[6]|7[1-8]|8[0-9]|9[1,3,5,7,8,9])\\d{8}$");
            if (flag) {
                //查询手机号是否存在,手机号不能重复
                boolean repeated = studentService.findStudentByPhone(phone);
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
    public void showAllClasses(){
        ClassesService classesService = new ClassesServiceImpl();
        List<Classes> classesList = classesService.queryAllClasses();
        for (Classes classes:classesList){
            System.out.println(classes);
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
    public void editStudentView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>学生信息管理>修改学生信息**********");
            System.out.println("请选择功能：1 修改姓名  2 修改性别 3 修改密码 4 修改电话 5 修改地址 6 修改班级 0 返回上一级 ");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入学生编号：");
                    int id = scanner.nextInt();
                    System.out.println("请输入学生新姓名：");
                    String name = scanner.next();
                    if (studentService.editNameById(id,name)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 2:
                    System.out.println("请输入学生编号：");
                    int id1 = scanner.nextInt();
                    System.out.println("请输入学生新性别：");
                    String gender = checkGender();
                    if (studentService.editGenderById(id1,gender)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 3:
                    System.out.println("请输入学生编号：");
                    int id2 = scanner.nextInt();
                    System.out.println("请输入学生新密码：");
                    String password = scanner.next();
                    if (studentService.editPasswordById(id2,password)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 4:
                    System.out.println("请输入学生编号：");
                    int id3 = scanner.nextInt();
                    System.out.println("请输入学生新电话：");
                    String phone = checkStudentPhone();
                    if (studentService.editPhoneById(id3,phone)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 5:
                    System.out.println("请输入学生编号：");
                    int id4 = scanner.nextInt();
                    System.out.println("请输入学生新地址：");
                    String address = scanner.next();
                    if (studentService.editAddressById(id4,address)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 6:
                    System.out.println("请输入学生编号：");
                    int id5 = scanner.nextInt();
                    System.out.println("请输入学生新班级：");
                    System.out.println("请选择班级编号：");
                    showAllClasses();
                    int classId = scanner.nextInt();
                    if (studentService.editClassIdById(id5,classId)){
                        System.out.println("修改成功");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }
    public void removeStudentView(){
        while (true){
            System.out.println("当前所在位置：**********管理员系统>学生信息管理>删除学生信息**********");
            System.out.println("*******请选择功能：1 删除学生 0 返回上一级*********");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    removeStudent();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }
    public void removeStudent(){
        System.out.println("请输入学生编号：");
        int id = scanner.nextInt();
        if (studentService.deleteStudent(id)){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }
}
