package com.nyist.download.Dao;

import com.nyist.download.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CateDao extends JpaRepository<Category,String> {

}
