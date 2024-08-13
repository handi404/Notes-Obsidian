package service.impl;

import dao.ManagerDao;
import dao.impl.ManagerDaoImpl;
import pojo.Manager;
import service.ManagerService;

public class ManagerServiceImpl implements ManagerService {
    private ManagerDao managerDao = new ManagerDaoImpl();
    @Override
    public Manager login(Object[] userInfo) {
        return managerDao.findManagerByUsernameAndPassword(userInfo);
    }

    @Override
    public boolean editPasswordByIdAndPassword(int id, String password, String newPassword) {
        if (managerDao.UpdatePasswordByIdAndPassword(id,password,newPassword) > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean queryManagerByIdAndPassword(int id, String password) {
        if (managerDao.SelectManagerByIdAndPassword(id,password) != null){
            return true;
        }else {
            return false;
        }
    }
}
