package com.nyist.download.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nyist.download.util.DateFormat;
import com.nyist.download.util.FtpUtil;
import com.nyist.download.util.IDUtils;
import com.nyist.download.util.NyistResult;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Download implements Serializable {
    private String id;
    private String title;
    private String dDesc;
    private String name;
    private String dwDate;
    private Integer commentCounts;
    private Integer downCounts;
    private Integer thumbCounts;
    private Integer isTop;
    private Integer isPass;
    private String fileUrl;
    private Collection<Comment> commentsById;
    private TUser tUserByUid;
    private Category categoryByCid;
    private String loveUser;


    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Basic
    @Column(name = "love_user", nullable = true, length = -1)
    public String getLoveUser() {
        return loveUser;
    }

    public void setLoveUser(String loveUser) {
        this.loveUser = loveUser;
    }

    @Basic
    @Column(name = "d_desc", nullable = true, length = 255)
    public String getdDesc() {
        return dDesc;
    }

    public void setdDesc(String dDesc) {
        this.dDesc = dDesc;
    }

    @Basic
    @Column(name = "dw_date", nullable = true, length = 255)
    public String getDwDate() {
        return dwDate;
    }

    public void setDwDate(String dwDate) {
        this.dwDate = dwDate;
    }

    @Basic
    @Column(name = "comment_counts", nullable = true)
    public Integer getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    @Basic
    @Column(name = "down_counts", nullable = true)
    public Integer getDownCounts() {
        return downCounts;
    }

    public void setDownCounts(Integer downCounts) {
        this.downCounts = downCounts;
    }

    @Basic
    @Column(name = "thumb_counts", nullable = true)
    public Integer getThumbCounts() {
        return thumbCounts;
    }

    public void setThumbCounts(Integer thumbCounts) {
        this.thumbCounts = thumbCounts;
    }

    @Basic
    @Column(name = "is_top", nullable = true)
    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    @Basic
    @Column(name = "is_pass", nullable = true)
    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    @Basic
    @Column(name = "file_url", nullable = true, length = 255)
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Download download = (Download) o;

        if (id != null ? !id.equals(download.id) : download.id != null) return false;
        if (title != null ? !title.equals(download.title) : download.title != null) return false;
        if (dDesc != null ? !dDesc.equals(download.dDesc) : download.dDesc != null) return false;
        if (dwDate != null ? !dwDate.equals(download.dwDate) : download.dwDate != null) return false;
        if (commentCounts != null ? !commentCounts.equals(download.commentCounts) : download.commentCounts != null)
            return false;
        if (downCounts != null ? !downCounts.equals(download.downCounts) : download.downCounts != null) return false;
        if (thumbCounts != null ? !thumbCounts.equals(download.thumbCounts) : download.thumbCounts != null)
            return false;
        if (isTop != null ? !isTop.equals(download.isTop) : download.isTop != null) return false;
        if (fileUrl != null ? !fileUrl.equals(download.fileUrl) : download.fileUrl != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (dDesc != null ? dDesc.hashCode() : 0);
        result = 31 * result + (dwDate != null ? dwDate.hashCode() : 0);
        result = 31 * result + (commentCounts != null ? commentCounts.hashCode() : 0);
        result = 31 * result + (downCounts != null ? downCounts.hashCode() : 0);
        result = 31 * result + (thumbCounts != null ? thumbCounts.hashCode() : 0);
        result = 31 * result + (isTop != null ? isTop.hashCode() : 0);
        result = 31 * result + (fileUrl != null ? fileUrl.hashCode() : 0);
        return result;
    }


    @JsonIgnore
    @OneToMany(mappedBy = "downloadByDid")
    public Collection<Comment> getCommentsById() {
        return commentsById;
    }

    public void setCommentsById(Collection<Comment> commentsById) {
        this.commentsById = commentsById;
    }

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    public TUser gettUserByUid() {
        return tUserByUid;
    }


    public void settUserByUid(TUser tUserByUid) {
        this.tUserByUid = tUserByUid;
    }


    @ManyToOne
    @JoinColumn(name = "cid", referencedColumnName = "id")
    public Category getCategoryByCid() {
        return categoryByCid;
    }

    public void setCategoryByCid(Category categoryByCid) {
        this.categoryByCid = categoryByCid;
    }


}
