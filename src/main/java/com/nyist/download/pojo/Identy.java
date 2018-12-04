package com.nyist.download.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Identy implements Serializable {
    private int id;
    private Integer maxScore;
    private Integer minScore;
    private String name;
    private Collection<TUser> tUsersById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "max_score", nullable = true)
    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    @Basic
    @Column(name = "min_score", nullable = true)
    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identy identy = (Identy) o;

        if (id != identy.id) return false;
        if (maxScore != null ? !maxScore.equals(identy.maxScore) : identy.maxScore != null) return false;
        if (minScore != null ? !minScore.equals(identy.minScore) : identy.minScore != null) return false;
        if (name != null ? !name.equals(identy.name) : identy.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (maxScore != null ? maxScore.hashCode() : 0);
        result = 31 * result + (minScore != null ? minScore.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    @OneToMany(mappedBy = "identyByIid")
    public Collection<TUser> gettUsersById() {
        return tUsersById;
    }


    public void settUsersById(Collection<TUser> tUsersById) {
        this.tUsersById = tUsersById;
    }
}
