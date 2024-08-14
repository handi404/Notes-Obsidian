package view;

import pojo.*;
import service.*;
import service.impl.*;
import utils.DataUtil;
import utils.ScannerUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainView {
    private Scanner scanner = ScannerUtils.scanner;
    TeacherService teacherService = new TeacherServiceImpl();
    CourseService courseService = new CourseServiceImpl();
    ClassesService classesService = new ClassesServiceImpl();
    StudentService studentService = new StudentServiceImpl();
    ScoreService scoreService = new ScoreServiceImpl();
    public void mainView(){
        while (true){
            System.out.println("****************************教务管理系统*************************");
            System.out.println("请选择：1 教师登录 2 学生登录 3 管理员登录 0 退出系统");
            System.out.println("****************************************************************");
            int choice = scanner.nextInt();
            if (choice==0){
                return;
            }
            String[] userInfo = new String[2];
            System.out.println("***********请输入账号**************");
            String name = scanner.next();
            System.out.println("***********请输入密码**************");
            String password = scanner.next();
            userInfo[0] = name;
            userInfo[1] = password;
            System.out.println("***********输入的账号密码是:"+ Arrays.toString(userInfo)+"***********");
            String mess = "";
            switch (choice){
                case 1:
                    //TODO: 教师登录
                    Teacher teacher = new TeacherServiceImpl().login(userInfo);
                    if (teacher!=null){
                        System.out.println("登陆的教师信息"+teacher);
                    }
                    teacherPage(teacher);
                    break;
                case 2:
                    //TODO: 学生登录
                    Student student = new StudentServiceImpl().login(userInfo);
                    if (student!=null){
                        System.out.println("登陆的学生信息"+student);
                    }
                    studentPage(student);
                    break;
                case 3:
                    //TODO:管理员登录
                    Manager manager = new ManagerServiceImpl().login(userInfo);
                    if (manager!=null){
                        System.out.println("登录的管理员信息"+manager);
                    }
                    managerPage(manager);
                    break;
                default:
                    mess = "功能暂未开放";
            }
            System.out.println(mess);
        }
    }
    public void teacherPage(Teacher teacher){
        Teacher loginteacher = teacher;
        while (true){
            System.out.println("****************教务系统->教师系统***************");
            System.out.println("***************当前教师用户:" + teacher.getName() + "****************");
            System.out.println("************************************************");
            System.out.println("请选择功能：1 个人中心 2 成绩管理   0 退出系统");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    //TODO: 个人中心
                    myselfView(loginteacher);
                    break;
                case 2:
                    //TODO: 成绩管理
                    scoreView(loginteacher);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
            }
        }
    }
    public void myselfView(Teacher loginteacher){
        TeacherView teacherView = new TeacherView();
        System.out.println("*********************当前所在位置：管理员系统-->个人信息*****************");
        System.out.println("基本信息："+loginteacher);
        Course course = courseService.queryCourseById(loginteacher.getCid());
        System.out.println("教授课程:"+course.getCname()+"\t"+"类型:"+course.getType());
        System.out.println("所带班级：");
        List<Map<String, Object>> classes = classesService.queryClassesByTeacherId(loginteacher.getId());
        if (classes.size()==0){
            System.out.println("该教师未带班级");
        }
        for (Map<String, Object> map : classes) {
            System.out.print(map.get("name")+"\t");
        }
        System.out.println();
        System.out.println("******************************************************");
        while (true){
            System.out.println("请选择功能：1 修改密码 2 修改电话 3 修改地址 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入新密码");
                    String password = scanner.next();
                    if (teacherService.editPasswordById(loginteacher.getId(),password)&&password.length()>=6){
                        System.out.println("密码修改完成，请牢记新密码:"+password);
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 2:
                    System.out.println("请输入新电话");
                    String phone = teacherView.checkTeacherPhone();
                    if (teacherService.editPhoneById(loginteacher.getId(),phone)){
                        System.out.println("电话修改完成");
                    }else {
                        System.out.println("修改失败,密码长度不可小于6位");
                    }
                    break;
                case 3:
                    System.out.println("请输入新地址");
                    String address = scanner.next();
                    if (teacherService.editAddressById(loginteacher.getId(),address)){
                        System.out.println("地址修改完成");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
            }
        }
    }
    public void scoreView(Teacher loginteacher){
        ScoreView scoreView = new ScoreView();
        System.out.println("*********************当前所在位置：管理员系统-->成绩管理*****************");
        while (true){
            System.out.println("请选择功能:1 查询授课成绩 2 添加学生成绩 3 修改学生成绩  0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    scoreView.queryScoreView(loginteacher);
                    break;
                case 2:
//                    addScoreView();
                    scoreView.addScoreView(loginteacher);
                    break;
                case 3:
//                    modifyScoreView();
                    scoreView.modifyScoreView(loginteacher);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
            }
        }
    }
    public void studentPage(Student student){
        Student loginStudent = student;
        while (true){
            System.out.println("****************教务系统-学生系统***************");
            System.out.println("***************当前用户为:" + student.getName() + "****************");
            System.out.println("************************************************");
            System.out.println("请选择功能：1 个人中心 2 成绩管理  3 选课 0 退出系统");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    myselfView1(loginStudent);
                    break;
                case 2:
                    System.out.println("您的成绩如下");
                    List<Score> scoreList = scoreService.queryScoreBySid(loginStudent.getId());
                    if (scoreList.size()==0){
                        System.out.println("暂未查询到课程成绩");
                    }else {
                        for (Score score : scoreList){
                            Course course = courseService.queryCourseById(score.getCid());
                            System.out.println("课程名称:"+course.getCname()+"\t成绩:"+score.getGrade());
                        }
                    }
                    break;
                case 3:
                    electiveCourseView(loginStudent);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
            }
        }
    }
    public void myselfView1(Student loginStudent){
        StudentView studentView = new StudentView();
        System.out.println("当前所在位置：**********学生系统-->个人信息管理**********");
        System.out.println("基本信息：" + loginStudent);
        while (true){
            System.out.println("请选择功能：1 修改密码 2 修改电话 3 修改地址 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    System.out.println("请输入新密码");
                    String password = scanner.next();
                    if (studentService.editPasswordById(loginStudent.getId(),password) && password.length()>=6){
                        System.out.println("密码修改完成，请牢记新密码:"+password);
                    }else {
                        System.out.println("修改失败,密码长度不小于6位");
                    }
                    break;
                case 2:
                    System.out.println("请输入新电话");
                    String phone = studentView.checkStudentPhone();
                    if (studentService.editPhoneById(loginStudent.getId(),phone)){
                        System.out.println("电话修改完成");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 3:
                    System.out.println("请输入新地址");
                    String address = scanner.next();
                    if (studentService.editAddressById(loginStudent.getId(),address)){
                        System.out.println("地址修改完成");
                    }else {
                        System.out.println("修改失败");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
            }
        }
    }
    public void electiveCourseView(Student loginStudent){
        System.out.println("当前所在位置：*********学生系统-->选课管理**********");
        while (true){
            List<Course> courseList = courseService.queryCourseByStudentId(loginStudent.getId());
            if (courseList == null || courseList.size() == 0){
                System.out.println("暂未进行课程选修！请即时选修");
                System.out.println("你可选修的课程如下:");
                List<Course> courseList1 = courseService.findCourseBySid(loginStudent.getId());
                if (courseList1.size() == 0){
                    System.out.println("没有新的选修课程信息，请过段时间再进行尝试！");
                    return;
                }
                ArrayList<Object> tids = new ArrayList<>();
                for (Course course : courseList1){
                    Teacher teacher = teacherService.queryTeacherByClassIdAndCourseId(loginStudent.getClassId(),course.getId());
                    System.out.println("课程名称："+course.getCname()+"\t"+"教师："+teacher.getName()+"\t"+"教师编号："+teacher.getId());
                    tids.add(teacher.getId());
                }
                System.out.println("请输入选修课程的教师编号：");
                int tid = 0;
                while (true){
                    tid = scanner.nextInt();
                    if (tids.contains(tid)){
                        break;
                    }
                    System.out.println("你选择的课程教师不在选修列表内，请重新选择：");
                }
                if (courseService.addElectiveCourse(loginStudent.getId(),tid)){
                    System.out.println("选修成功");
                }else {
                    System.out.println("选修失败");
                }
            }else {
                System.out.println("你已选修的课程如下:");
                for (Course course : courseList){
                    Teacher teacher = teacherService.queryTeacherByClassIdAndCourseId(loginStudent.getClassId(),course.getId());
                    System.out.println("课程名称:"+course.getCname()+"\t课程类型:"+course.getType()+"\t"+"教师:"+teacher.getName());
                }
                return;
            }
        }
    }
    public void managerPage(Manager manager) {
        Manager loginManager = manager;
        while (true) {
            System.out.println("****************教务系统->管理员系统***************");
            System.out.println("***************当前管理员用户:" + manager.getUsername() + "****************");
            System.out.println("************************************************");
            System.out.println("请选择功能：1 教师管理 2 学生管理 3 课程管理 4 班级管理 5 密码修改 0 退出系统");
            int num = scanner.nextInt();
            switch (num) {
                case 1:
                    //TODO teacherView();
                    teacherView();
                    break;
                case 2:
                    //TODO studentView();
                    studentView();
                    break;
                case 3:
                    //TODO courseView();
                    courseView();
                    break;
                case 4:
                    //TODO classesView();
                    classesView();
                    break;
                case 5:
                    editPasswordView(loginManager);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("功能暂未开放，请重新选择");
                    break;
            }
        }
    }
    public void teacherView(){
        TeacherView teacherView = new TeacherView();
        while (true) {
            System.out.println("***************管理员系统->教师信息管理*******************");
            System.out.println("请选择功能:1 查询教师信息 2 添加教师信息 3 修改教师信息 4 删除教师信息 0 返回上一级");
            int num = scanner.nextInt();
            switch (num) {
                case 1:
                    //TODO queryTeachersView();
                    queryTeacherView();
                    break;
                case 2:
                    //TODO addTeacherView();
                    teacherView.addTeacherView();
                    break;
                case 3:
                    //TODO editTeacherView();
                    teacherView.editTeacherView();
                    break;
                case 4:
                    //TODO removeTeacherView();
                    teacherView.removeTeacherView();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("该功能暂未开放，请重新选择");
                    break;
            }
        }
    }
    public void queryTeacherView(){
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->教师信息管理-->教师信息查询*****************");
            System.out.println("******请选择功能：1.全部查询  2.根据姓名查询  3.根据班级查询  4.根据课程查询  0 返回上一级******");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    List<Teacher> teacherList = teacherService.queryAllTeacher();
                    if (teacherList.size() == 0){
                        System.out.println("查无教师");
                    }
                    showTeacherInfo(teacherList);
                    break;
                case 2:
                    showTeacherByName();
                    break;
                case 3:
                    showTeacherAndClassName();
                    break;
                case 4:
                    showTeacherAndCourseName();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("请输入正确的操作序号！");
                    break;
            }
        }
    }
    public void showTeacherInfo(List<Teacher> teacherList){
        System.out.println("**********************查询结果如下：*******************************");
        for (Teacher teacher:teacherList){
            System.out.println(teacher);
        }
    }
    public void showTeacherByName(){
        System.out.println("********************请输入教师姓名*********************");
        String name = scanner.next();
        List<Teacher> teacherList = teacherService.queryTeacherByName(name);
        showTeacherInfo(teacherList);
    }
    public void showTeacherAndClassName(){
        System.out.println("********************请输入班级名称*********************");
        String name = scanner.next();
        List<Map<String,Object>> teacherList = teacherService.queryTeacherByClassName(name);
        for (Map<String,Object> map:teacherList){
            System.out.println("教师编号："+map.get("id")+",姓名："+map.get("name")+",性别："+map.get("gender")+",地址"+map.get("address")+
                    ",入职日期："+ DataUtil.format((Date)map.get("joinedDate"),"yyyy-MM-dd")+",所授班级:"+map.get("className"));
        }
    }
    public void showTeacherAndCourseName(){
        System.out.println("********************请输入课程名称*********************");
        String name = scanner.next();
        List<Map<String,Object>> teacherList = teacherService.queryTeacherByCourseName(name);
        for (Map<String,Object> map:teacherList){
            System.out.println("教师编号："+map.get("id")+",姓名："+map.get("name")+",性别："+map.get("gender")+",地址"+map.get("address")+
                    ",入职日期："+ DataUtil.format((Date)map.get("joinedDate"),"yyyy-MM-dd")+",所授课程:"+map.get("courseName"));
        }
    }

    public void studentView(){
        StudentView studentView = new StudentView();
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->学生信息管理*****************");
            System.out.println("请选择功能:1 查询学生信息 2 添加学生信息 3 修改学生信息 4 删除学生信息 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    studentView.queryStudentView();
                    break;
                case 2:
                    studentView.addStudentView();
                    break;
                case 3:
                    studentView.editStudentView();
                    break;
                case 4:
                    studentView.removeStudent();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("请输入正确的操作序号！");
                    break;
            }
        }
    }
    public void courseView(){
        CourseView courseView = new CourseView();
        while (true){
            System.out.println("*********************当前所在位置：管理员系统-->课程信息管理*****************");
            System.out.println("请选择功能:1 查询课程信息 2 添加课程信息 3 修改课程信息 4 删除课程信息 0 返回上一级");
            int num = scanner.nextInt();
            switch (num){
                case 1:
                    courseView.queryCourseView();
                    break;
                case 2:
                    courseView.addCourseView();
                   break;
                case 3:
                    courseView.editCourseView();
                    break;
                case 4:
                    courseView.removeCourseView();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("请输入正确的操作序号！");
               }
        }
    }
    public void classesView(){
        ClassesView classesView = new ClassesView();
        System.out.println("当前所在位置：**********管理员系统>班级信息管理**********");
        System.out.println("请选择功能:1 查询班级信息 2 添加班级信息 3 修改班级信息 4 删除班级信息 0 返回上一级");
        int num = scanner.nextInt();
        switch (num){
            case 1:
                classesView.queryClassesView();
                break;
            case 2:
                classesView.addClassesView();
                break;
            case 3:
                classesView.editClassesView();
                break;
            case 4:
                classesView.removeClassesView();
                break;
            case 0:
                return;
            default:
                System.out.println("请输入正确的操作序号！");
        }
    }
    public void editPasswordView(Manager loginManager){
        ManagerService managerService = new ManagerServiceImpl();
        System.out.println("*********************当前所在位置：管理员系统-->修改密码*****************");
        System.out.println("请选择功能: 1 修改密码  0 返回上一级");
        int num = scanner.nextInt();
        switch (num){
            case 1:
                while (true){
                    System.out.println("请输入密码");
                    String password = scanner.next();
                    boolean bl = managerService.queryManagerByIdAndPassword(loginManager.getId(), password);
                    if (bl){
                        while (true){
                            System.out.println("请输入新密码");
                            String newPassword = scanner.next();
                            if (newPassword.length() >= 6){
                                System.out.println("请再次输入新密码");
                                String newPassword2 = scanner.next();
                                if (newPassword.equals(newPassword2)){
                                    managerService.editPasswordByIdAndPassword(loginManager.getId(), password,newPassword);
                                    System.out.println("密码修改完成，请牢记新密码:"+newPassword);
                                    return;
                                }else {
                                    System.out.println("两次输入密码不一致，请重新输入");
                                }
                            }else {
                                System.out.println("密码长度不能小于6位，请重新输入");
                            }
                        }
                    }else {
                        System.out.println("密码错误，请重新输入");
                    }
                }
            case 0:
                return;
            default:
                System.out.println("请输入正确的操作序号！");
        }
    }
}
