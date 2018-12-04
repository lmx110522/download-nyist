package com.nyist.download.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_message", schema = "download", catalog = "")
public class TMessage implements Serializable {
    private int id;
    private String content;
    private String mDate;
    private Integer isRead;
    private TUser tUserByUid;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "m_date", nullable = true, length = 255)
    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TMessage tMessage = (TMessage) o;

        if (id != tMessage.id) return false;
        if (content != null ? !content.equals(tMessage.content) : tMessage.content != null) return false;
        if (mDate != null ? !mDate.equals(tMessage.mDate) : tMessage.mDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        return result;
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
