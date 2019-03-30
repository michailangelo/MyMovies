/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Michailangelo
 */
@Entity
@Table(name = "MOVIE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m")
    , @NamedQuery(name = "Movie.findByMovieid", query = "SELECT m FROM Movie m WHERE m.movieid = :movieid")
    , @NamedQuery(name = "Movie.findByTitle", query = "SELECT m FROM Movie m WHERE m.title = :title")
    , @NamedQuery(name = "Movie.findByReleasedate", query = "SELECT m FROM Movie m WHERE m.releasedate = :releasedate")
    , @NamedQuery(name = "Movie.findByRating", query = "SELECT m FROM Movie m WHERE m.rating = :rating")
    , @NamedQuery(name = "Movie.findByOverview", query = "SELECT m FROM Movie m WHERE m.overview = :overview")})
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "MOVIEID")
    private Integer movieid;
    @Basic(optional = false)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @Column(name = "RELEASEDATE")
    @Temporal(TemporalType.DATE)
    private Date releasedate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RATING")
    private Float rating;
    @Column(name = "OVERVIEW")
    private String overview;
    @JoinColumn(name = "FAVORITELISTID", referencedColumnName = "FAVORITELISTID")
    @ManyToOne
    private Favoritelist favoritelistid;
    @JoinColumn(name = "GENREID", referencedColumnName = "GENREID")
    @ManyToOne(optional = false)
    private Genre genreid;

    public Movie() {
    }

    public Movie(Integer movieid) {
        this.movieid = movieid;
    }

    public Movie(Integer movieid, String title, Date releasedate) {
        this.movieid = movieid;
        this.title = title;
        this.releasedate = releasedate;
    }

    public Integer getMovieid() {
        return movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Favoritelist getFavoritelistid() {
        return favoritelistid;
    }

    public void setFavoritelistid(Favoritelist favoritelistid) {
        this.favoritelistid = favoritelistid;
    }

    public Genre getGenreid() {
        return genreid;
    }

    public void setGenreid(Genre genreid) {
        this.genreid = genreid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movieid != null ? movieid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        if ((this.movieid == null && other.movieid != null) || (this.movieid != null && !this.movieid.equals(other.movieid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Movie[ movieid=" + movieid + " ]";
    }
    
}
