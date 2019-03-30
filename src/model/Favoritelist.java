/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "FAVORITELIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favoritelist.findAll", query = "SELECT f FROM Favoritelist f")
    , @NamedQuery(name = "Favoritelist.findByFavoritelistid", query = "SELECT f FROM Favoritelist f WHERE f.favoritelistid = :favoritelistid")
    , @NamedQuery(name = "Favoritelist.findByName", query = "SELECT f FROM Favoritelist f WHERE f.name = :name")})
public class Favoritelist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "FAVORITELISTID")
    private Integer favoritelistid;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "favoritelistid")
    private List<Movie> movieList;

    public Favoritelist() {
    }

    public Favoritelist(Integer favoritelistid) {
        this.favoritelistid = favoritelistid;
        movieList = new ArrayList<>();
    }

    public Favoritelist(Integer favoritelistid, String name) {
        this.favoritelistid = favoritelistid;
        this.name = name;
        movieList = new ArrayList<>();
    }

    public Integer getFavoritelistid() {
        return favoritelistid;
    }

    public void setFavoritelistid(Integer favoritelistid) {
        this.favoritelistid = favoritelistid;
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

    public void addMovie(Movie mv) {
        movieList.add(mv);
    }

    public void removeMovie(Movie mv) {
        movieList.remove(mv);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favoritelistid != null ? favoritelistid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favoritelist)) {
            return false;
        }
        Favoritelist other = (Favoritelist) object;
        if ((this.favoritelistid == null && other.favoritelistid != null) || (this.favoritelistid != null && !this.favoritelistid.equals(other.favoritelistid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

}
