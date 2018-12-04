package com.nyist.download.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Category implements Serializable {
    private String id;
    private String name;
    private Integer counts;
    private Integer socre;
    private Collection<Download> downloadsById;

    @Id
    @Column(name = "id", nullable = false, length = 255)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "counts", nullable = true)
    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    @Basic
    @Column(name = "socre", nullable = true)
    public Integer getSocre() {
        return socre;
    }

    public void setSocre(Integer socre) {
        this.socre = socre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        if (counts != null ? !counts.equals(category.counts) : category.counts != null) return false;
        if (socre != null ? !socre.equals(category.socre) : category.socre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (counts != null ? counts.hashCode() : 0);
        result = 31 * result + (socre != null ? socre.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "categoryByCid")
    public Collection<Download> getDownloadsById() {
        return downloadsById;
    }

    public void setDownloadsById(Collection<Download> downloadsById) {
        this.downloadsById = downloadsById;
    }
}
