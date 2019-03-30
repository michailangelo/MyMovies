package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Favoritelist;
import model.Genre;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-12T10:02:35")
@StaticMetamodel(Movie.class)
public class Movie_ { 

    public static volatile SingularAttribute<Movie, Genre> genreid;
    public static volatile SingularAttribute<Movie, String> overview;
    public static volatile SingularAttribute<Movie, Float> rating;
    public static volatile SingularAttribute<Movie, Favoritelist> favoritelistid;
    public static volatile SingularAttribute<Movie, Integer> movieid;
    public static volatile SingularAttribute<Movie, Date> releasedate;
    public static volatile SingularAttribute<Movie, String> title;

}