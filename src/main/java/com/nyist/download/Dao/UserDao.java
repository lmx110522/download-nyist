package com.nyist.download.Dao;

import com.nyist.download.pojo.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<TUser,String> {


    TUser findByPhone(String phone);
}
