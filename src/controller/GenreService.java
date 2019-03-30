package controller;

import javax.persistence.*;

import model.Genre;

import java.util.List;

/**
 * Service Class for Operating on Genre entities
 *
 * @author Michailangelo
 */
public class GenreService {

    protected EntityManager em;

    public GenreService(EntityManager em) {
        this.em = em;
    }

    public Genre createGenre(int id, String name) {
        Genre gen = new Genre(id);
        gen.setName(name);
        em.persist(gen);
        return gen;

    }

    public void removeGenre(int id) {
        Genre gen = findGenre(id);
        if (gen != null) {
            em.remove(gen);
        }
    }

    public void dropTable() {
        em.createQuery("DELETE FROM Genre").executeUpdate();
    }

    public Genre findGenre(int id) {
        return em.find(Genre.class, id);
    }

    public List<Genre> findAllGenres() {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

}
