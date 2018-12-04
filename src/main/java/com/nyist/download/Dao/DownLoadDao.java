package com.nyist.download.Dao;

import com.nyist.download.pojo.Category;
import com.nyist.download.pojo.Download;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DownLoadDao extends JpaRepository<Download,String> {

    Page<Download> findDownloadsByIsPass(Integer isPass, Pageable pageable);

    Page<Download> findDownloadsByCategoryByCidAndIsPass(Category category,Integer isPass,Pageable pageable);

    List<Download> findDownloadsTop10ByIsPassOrderByDwDateDesc(Integer isPass);

    List<Download> findDownloadsTop10ByIsPassOrderByDownCountsDesc(Integer isPass);

    List<Download> findDownloadsTop10ByCategoryByCidAndIsPassOrderByDwDateDesc(Category category,Integer isPass);

}
