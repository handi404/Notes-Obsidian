
# MyBatis注解开发

MyBatis可以使用xml文件来写sql语句，也可以通过注解来编写简单的sql语句，参考[官方文档](http://www.mybatis.org/mybatis-3/zh/java-api.html) 

在之前的开发中，我们使用mybatis，需要

1，配置文件，

2，然后创建dao接口，定义方法

3，再然后就要创建mapper.xml文件，在mapper.xml文件中编写sql语句，

4，最后再把mapper文件配置在mybatis主配置文件中就可以进行测试了

使用注解的方式，我们可以在dao接口中直接在方法上写sql语句，不需要创建mapper.xml文件了

使用注解也需要在mybatis配置文件中配置mapper接口的package扫描

## 常用注解

### @Param()注解

语法:@Param(value="绑定值")可以简写为@Param("绑定值")

用于接口方法存在多个参数进行绑定传值

```java
public int selectByNameAndJob(@Param("name")int name,@Param("job")int job);
```

 

### @Select()注解

**语法**:@Select(value="查询语句")可以简写为@Select("查询语句")

等价于mapper.xml中的<select>

```java
    @Select("select * from emp")
    List<Emp> allEmp();
```

 


### @Insert()注解

**语法**:@Insert(value="添加语句")可以简写为@Insert("添加语句")

等价于mapper.xml中的<insert>

```java
    @Insert("insert into emp (ename,job,deptno,sal,hiredate) value(#{ename},#{job},#{deptno},#{sal},#{hiredate})")
    int insertEmp(Emp emp);
```


### @Update()注解

**语法**:@Update(value="修改语句")可以简写为@Update("修改语句")

等价于mapper.xml中的<update>

```java
    @Update("update emp set ename=#{ename} where empno=#{empno}")
    int updateEmp(Emp emp);
```

 

### @Delete()注解

**语法**:@Delete(value="删除语句")可以简写为@Select("删除语句")

等价于mapper.xml中的<delete>

```java
    @Delete("delete from emp where empno=#{empno}")
    int deleteEmp(int empno);
```

 

### @Results()注解

**语法**:@Results(id="唯一标识",value={result标签数组})

等价于mapper.xml中的<resultMap>

### @Result()注解

**语法**:@Result(id="true/false是否为主键",column="数据库列",property="属性名")

等价于mapper.xml中的<result>与<id>

```java
	@Results(id="empMap",value={
			@Result(id=true,column="eid",property="eid"),
			@Result( column="ename",property="ename"),
			@Result( column="ejob",property="ejob")
	})
```

[ 

### @resultMap()注解

**语法**:@resultMap(value="返回结果集映射id")可以简写为@resultMap("返回结果集映射id")

等价于mapper.xml中的resultMap属性

```java
    @Select("select * from emp")
    @ResultMap("empMap")
    List<Emp> allEmp();
```


### 使用注解完成动态sql的书写

使用Mybatis注解实现sql语句，但是有些时候有些字段是空的，这时候这个空的字段就要从条件查询语句中删除，这个时候就需要用到动态Sql。

使用方式很简单在原有的基础上使用{<script> sql语句 </script>}进行包裹即可

```java
@Select({"<script> " +
        "select * from t_user " +
        "where  1=1 " +
        "<if test='userId!=null'> and id = #{userId}</if> " +
        "</script>"})
```

 


### 使用注解完成一对一sql的书写

### @One()注解

**语法**:@One(select="执行方法",fetchType="加载方式(FetchType.LAZY深入懒加载)")

one是@Result标签的一个属性,添加后相当于将result标签转换为mapper.xml中的<association>

```java
	@Results(id = "empMap", value = { 
			@Result(id = true, column = "eid", property = "eid"),
			@Result(column = "ename", property = "ename"), 
			@Result(column = "ejob", property = "ejob") ,
			@Result(column = "dId", property = "dept",one=@One(select="selectById",fetchType=FetchType.LAZY))
			 })
	@Select("select * from emp")
	public ArrayList<Emp> selectEmp();
	
	@Select("select * from dept where did=#{did}")
	public Dept selectById(int did);
```

 

### 使用注解完成一对多sql的书写

### @Many()注解

**语法**:@Many(select="执行方法",fetchType="加载方式(FetchType.LAZY深入懒加载)")

Many是@Result标签的一个属性,添加后相当于将result标签转换为mapper.xml中的<colleaction>

```java
	@Results(id = "empMap", value = { 
			@Result(id = true, column = "eid", property = "eid"),
			@Result(column = "ename", property = "ename"), 
			@Result(column = "ejob", property = "ejob") ,
			@Result(column = "dId", property = "dept",many=@Many(select="selectById",fetchType=FetchType.LAZY))
			 })
	@Select("select * from emp")
	public ArrayList<Emp> selectEmp();
	
	@Select("select * from dept where did=#{did}")
	public ArrayList<Dept> selectById(int did);
```

**注意:在进行sql书写时常常将不同表sql书写在不同的位置,所以在进行一对一、一对多书写是select对应的值可以书写为接口全路径.方法名**




