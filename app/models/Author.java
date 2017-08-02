package models;

import com.fasterxml.jackson.annotation.JsonView;
import controllers.json.view.AuthorViews;
import controllers.json.view.BookViews;
import controllers.json.view.GenreViews;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Author entity representation.
 */
@Entity
public class Author extends NamedModel {

    /**
     * Author's last name.
     */
    @JsonView({AuthorViews.Public.class, BookViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "author.lastName")
    @Size(min = 1, message = "author.lastName")
    public String lastName;

    /**
     * Author's first name.
     */
    @JsonView({AuthorViews.Public.class, BookViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "author.firstName")
    @Size(min = 1, message = "author.firstName")
    public String firstName;

    /**
     * Author's middle name.
     */
    @JsonView({AuthorViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "author.middleName")
    @Size(min = 1, message = "author.middleName")
    public String middleName;

    /**
     * Books written by this author.
     */
    @JsonView({AuthorViews.Public.class})
    @OneToMany
    public Set<Book> bookCollection;
}
