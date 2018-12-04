package com.nyist.download.service;

import com.nyist.download.Dao.CateDao;
import com.nyist.download.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CateService {

    @Autowired
    private CateDao cateDao;

    public List<Category> findAll() {

        List<Category> categoryList = cateDao.findAll();

        return categoryList;
    }
}
