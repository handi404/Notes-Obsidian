package dao;

import pojo.Manager;

import java.util.List;

public interface ManagerDao {
    public int InsertManager(Manager manager);
    public int DeleteManagerById(int id);
    public int UpdateManager(Manager manager);
    public Manager SelectManagerById(int id);
    public Manager SelectManagerByIdAndPassword(int id,String password);
    public List<Manager> SelectAllManager();
    Manager findManagerByUsernameAndPassword(Object[] userInfo);
    public int UpdatePasswordByIdAndPassword(int id ,String password,String newPassword);
}
