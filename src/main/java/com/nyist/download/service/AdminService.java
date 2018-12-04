package com.nyist.download.service;

import com.nyist.download.Dao.AdminDao;
import com.nyist.download.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    public Admin login(Admin admin) {
       return  adminDao.findByUsernameAndPassword(admin.getUsername(), admin.getPassword());
    }
}
