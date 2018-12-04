package com.nyist.download.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "t_user", schema = "download", catalog = "")
public class TUser implements Serializable {
    private String id;
    private String headImg;
    private String phone;
    private String username;
    private String password;
    private String birth;
    private String uDesc;
    private Integer isRead;
    private Integer uploadCounts;
    private Integer downCounts;
    private Integer score;
    private Collection<Comment> commentsById;
    private Collection<Download> downloadsById;
    private Collection<TMessage> tMessagesById;
    private Identy identyByIid;

    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "head_img", nullable = true, length = 255)
    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 255)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "birth", nullable = true, length = 255)
    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Basic
    @Column(name = "u_desc", nullable = true, length = 255)
    public String getuDesc() {
        return uDesc;
    }

    public void setuDesc(String uDesc) {
        this.uDesc = uDesc;
    }

    @Basic
    @Column(name = "is_read", nullable = true)
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    @Basic
    @Column(name = "upload_counts", nullable = true)
    public Integer getUploadCounts() {
        return uploadCounts;
    }

    public void setUploadCounts(Integer uploadCounts) {
        this.uploadCounts = uploadCounts;
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
    @Column(name = "score", nullable = true)
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TUser tUser = (TUser) o;

        if (id != null ? !id.equals(tUser.id) : tUser.id != null) return false;
        if (headImg != null ? !headImg.equals(tUser.headImg) : tUser.headImg != null) return false;
        if (phone != null ? !phone.equals(tUser.phone) : tUser.phone != null) return false;
        if (username != null ? !username.equals(tUser.username) : tUser.username != null) return false;
        if (password != null ? !password.equals(tUser.password) : tUser.password != null) return false;
        if (birth != null ? !birth.equals(tUser.birth) : tUser.birth != null) return false;
        if (uDesc != null ? !uDesc.equals(tUser.uDesc) : tUser.uDesc != null) return false;
        if (isRead != null ? !isRead.equals(tUser.isRead) : tUser.isRead != null) return false;
        if (uploadCounts != null ? !uploadCounts.equals(tUser.uploadCounts) : tUser.uploadCounts != null) return false;
        if (downCounts != null ? !downCounts.equals(tUser.downCounts) : tUser.downCounts != null) return false;
        if (score != null ? !score.equals(tUser.score) : tUser.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (headImg != null ? headImg.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (birth != null ? birth.hashCode() : 0);
        result = 31 * result + (uDesc != null ? uDesc.hashCode() : 0);
        result = 31 * result + (isRead != null ? isRead.hashCode() : 0);
        result = 31 * result + (uploadCounts != null ? uploadCounts.hashCode() : 0);
        result = 31 * result + (downCounts != null ? downCounts.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "tUserByUid")
    public Collection<Comment> getCommentsById() {
        return commentsById;
    }

    public void setCommentsById(Collection<Comment> commentsById) {
        this.commentsById = commentsById;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "tUserByUid")
    public Collection<Download> getDownloadsById() {
        return downloadsById;
    }

    public void setDownloadsById(Collection<Download> downloadsById) {
        this.downloadsById = downloadsById;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "tUserByUid")
    public Collection<TMessage> gettMessagesById() {
        return tMessagesById;
    }

    public void settMessagesById(Collection<TMessage> tMessagesById) {
        this.tMessagesById = tMessagesById;
    }

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "iid", referencedColumnName = "id")
    public Identy getIdentyByIid() {
        return identyByIid;
    }

    public void setIdentyByIid(Identy identyByIid) {
        this.identyByIid = identyByIid;
    }
}
