package com.nyist.download.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Comment implements Serializable {
    private String id;
    private String content;
    private String cDate;
    private Download downloadByDid;
    private TUser tUserByUid;

    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "c_date", nullable = true, length = 255)
    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null) return false;
        if (cDate != null ? !cDate.equals(comment.cDate) : comment.cDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (cDate != null ? cDate.hashCode() : 0);
        return result;
    }



    @ManyToOne
    @JoinColumn(name = "did", referencedColumnName = "id")
    public Download getDownloadByDid() {
        return downloadByDid;
    }

    public void setDownloadByDid(Download downloadByDid) {
        this.downloadByDid = downloadByDid;
    }



    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "id")
    public TUser gettUserByUid() {
        return tUserByUid;
    }

    public void settUserByUid(TUser tUserByUid) {
        this.tUserByUid = tUserByUid;
    }
}
