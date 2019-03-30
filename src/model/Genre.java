/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Michailangelo
 */
@Entity
@Table(name = "GENRE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g")
    , @NamedQuery(name = "Genre.findByGenreid", query = "SELECT g FROM Genre g WHERE g.genreid = :genreid")
    , @NamedQuery(name = "Genre.findByName", query = "SELECT g FROM Genre g WHERE g.name = :name")})
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "GENREID")
    private Integer genreid;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genreid")
    private List<Movie> movieList;

    public Genre() {
    }

    public Genre(Integer genreid) {
        this.genreid = genreid;
    }

    public Genre(Integer genreid, String name) {
        this.genreid = genreid;
        this.name = name;
    }

    public Integer getGenreid() {
        return genreid;
    }

    public void setGenreid(Integer genreid) {
        this.genreid = genreid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genreid != null ? genreid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genre)) {
            return false;
        }
        Genre other = (Genre) object;
        if ((this.genreid == null && other.genreid != null) || (this.genreid != null && !this.genreid.equals(other.genreid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

}
