package controller;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import model.Favoritelist;

import model.Genre;
import model.Movie;
/**
 * Service Class for Operating on Movie entities
 * @author Michailangelo
 */
public class MovieService {

    protected EntityManager em;

    public MovieService(EntityManager em) {
        this.em = em;
    }

    public Movie createMovie(int id, String title, Genre genre, Date date, float rating, String overview) {
        Movie mov = new Movie();
        mov.setMovieid(id);
        mov.setTitle(title);
        mov.setGenreid(genre);
        mov.setReleasedate(date);
        mov.setRating(rating);
        mov.setOverview(overview);
        em.persist(mov);
        return mov;

    }

    public void removeMovie(int id) {
        Movie mov = findMovie(id);
        if (mov != null) {
            em.remove(mov);
        }
    }
    /**
     * Method for setting the favoritelist of a Movie object
     * @param id
     * @param fv
     * @return Movie object
     */

    public Movie setFavoriteList(int id, Favoritelist fv) {
        Movie mv = findMovie(id);
        if (mv != null) {
            mv.setFavoritelistid(fv);
        }
        return mv;

    }

    public void dropTable() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
    }

    public Movie findMovie(int id) {
        return em.find(Movie.class, id);
    }

    public List<Movie> findAllMovies() {
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        return query.getResultList();
    }

}
