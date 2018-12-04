package com.nyist.download.Dao;

import com.nyist.download.pojo.Comment;
import com.nyist.download.pojo.Download;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment,String> {

    Page<Comment> findCommentByDownloadByDid(Download download, Pageable pageable);
}
