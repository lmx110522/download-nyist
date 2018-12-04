package com.nyist.download.Dao;

import com.nyist.download.pojo.TMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDao extends JpaRepository<TMessage,Integer> {

}
