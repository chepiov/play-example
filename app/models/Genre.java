package models;

import com.fasterxml.jackson.annotation.JsonView;
import controllers.json.view.AuthorViews;
import controllers.json.view.BookViews;
import controllers.json.view.GenreViews;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Genre entity representation.
 */
@Entity
public class Genre extends NamedModel {

    /**
     * Name of genre.
     */
    @JsonView({GenreViews.Public.class, BookViews.Internal.class, AuthorViews.Public.class})
    public String name;

    /**
     * Books of this genre.
     */
    @JsonView({GenreViews.Public.class})
    @ManyToMany
    public Set<Book> bookCollection;
}
