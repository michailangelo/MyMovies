/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.persistence.*;

import model.Favoritelist;

import java.util.List;
import model.Movie;

/**
 * Service Class for Operating on Favoritelist entities
 *
 * @author
 */
public class FavoriteListService {

    /**
     * The entity manager object that is needed to have access in the database
     *
     */
    protected EntityManager em;

    public FavoriteListService(EntityManager em) {
        this.em = em;
    }

    /**
     * Method for creating a Favoritelist
     *
     * @param id
     * @param name
     * @return Favorilist object
     */
    public Favoritelist createFavoritelist(int id, String name) {
        Favoritelist fl = new Favoritelist(id, name);
        em.persist(fl);
        return fl;

    }

    /**
     * Method for removing a Favoritelist
     *
     * @param id
     */
    public void removeFavoritelist(int id) {
        Favoritelist fl = findFavoritelist(id);
        if (fl != null) {
            em.remove(fl);
        }
    }

    /**
     * Method for updating the movie list of favoritelist
     *
     * @param id
     * @param movies
     * @return favoritelist object
     */
    public Favoritelist updateMovieList(int id, List<Movie> movies) {
        Favoritelist fl = em.find(Favoritelist.class, id);
        if (fl != null) {
            fl.setMovieList(movies);
        }
        return fl;
    }

    /**
     * Method for deleting all data of favoritelist table
     */
    public void dropTable() {
        em.createQuery("DELETE FROM Favoritelist").executeUpdate();
    }

    /**
     * Method for udpdating name field of favoritelist
     *
     * @param id
     * @param name
     * @return
     */
    public Favoritelist updateName(int id, String name) {
        Favoritelist fl = em.find(Favoritelist.class, id);
        if (fl != null) {
            fl.setName(name);
        }
        return fl;
    }

    /**
     * Method for adding a movie in favoritelist
     *
     * @param id
     * @param mv
     * @return favoritelist object
     */
    public Favoritelist addMovie(int id, Movie mv) {
        Favoritelist fl = em.find(Favoritelist.class, id);
        if (fl != null) {
            fl.addMovie(mv);
        }
        return fl;
    }

    /**
     * Method for removing a movie from favorite list
     *
     * @param id
     * @param mv
     * @return favoritelist object
     */
    public Favoritelist removeMovie(int id, Movie mv) {
        Favoritelist fl = em.find(Favoritelist.class, id);
        if (fl != null) {
            fl.removeMovie(mv);
        }
        return fl;
    }

    /**
     * Method for finding a favoritelist
     *
     * @param id
     * @return
     */
    public Favoritelist findFavoritelist(int id) {
        return em.find(Favoritelist.class, id);
    }

    /**
     * Method for issuing a query
     *
     * @return a List of favoritelist objects
     */
    public List<Favoritelist> findAllFavoritelists() {
        TypedQuery<Favoritelist> query = em.createQuery("SELECT f FROM Favoritelist f", Favoritelist.class);
        return query.getResultList();
    }

}
