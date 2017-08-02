package models;

import com.fasterxml.jackson.annotation.JsonView;
import controllers.json.view.AuthorViews;
import controllers.json.view.BookViews;
import controllers.json.view.GenreViews;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Book entity representation.
 */
@Entity
public class Book extends NamedModel {

    /**
     * Name of book.
     */
    @JsonView({BookViews.Public.class, AuthorViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "book.name")
    @Size(min = 1, message = "book.name")
    public String name;

    /**
     * Year of publishing.
     */
    @JsonView({BookViews.Public.class, AuthorViews.Public.class, GenreViews.Public.class})
    @Min(value = 1, message = "book.year")
    public int year;

    /**
     * Edition version.
     */
    @JsonView({BookViews.Public.class, AuthorViews.Public.class, GenreViews.Public.class})
    @Min(value = 1, message = "book.edition")
    public int edition;

    /**
     * Author. Each book has only one author.
     */
    @JsonView({BookViews.Public.class, GenreViews.Public.class})
    @ManyToOne
    @NotNull(message = "book.author")
    public Author author;

    /**
     * Genres of book. Each book has at least one genre.
     */
    @JsonView({BookViews.Internal.class, AuthorViews.Public.class})
    @ManyToMany
    @NotNull(message = "book.genre")
    @Size(min = 1, message = "book.genre")
    public Set<Genre> genreCollection;

}
