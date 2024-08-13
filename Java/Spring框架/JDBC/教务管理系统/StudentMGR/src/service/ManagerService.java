package service;

import pojo.Manager;

public interface ManagerService {
    public Manager login(Object[] userInfo);
    public boolean editPasswordByIdAndPassword(int id,String password,String newPassword);
    public boolean queryManagerByIdAndPassword(int id,String password);
}
