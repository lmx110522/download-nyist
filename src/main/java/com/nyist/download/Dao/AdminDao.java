package com.nyist.download.Dao;

import com.nyist.download.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<Admin,String> {

    Admin findByUsernameAndPassword(String username,String password);
}
