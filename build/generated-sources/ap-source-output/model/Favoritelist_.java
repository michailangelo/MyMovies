package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Movie;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-12T10:02:35")
@StaticMetamodel(Favoritelist.class)
public class Favoritelist_ { 

    public static volatile SingularAttribute<Favoritelist, String> name;
    public static volatile SingularAttribute<Favoritelist, Integer> favoritelistid;
    public static volatile ListAttribute<Favoritelist, Movie> movieList;

}