import models.Author;
import models.Book;
import models.Genre;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.test.WithApplication;
import repository.AuthorRepository;
import repository.BookRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Repositories tests.
 */
public class RepositoryTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void findBookByGenre() {
        final BookRepository bookRepository = app.injector().instanceOf(BookRepository.class);
        final CompletionStage<List<Book>> stage
                = bookRepository.byGenre("f022820c-cb84-4981-9184-46b6f9a17de8", 0, 10);

        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(books ->
                                books.size() == 1 &&
                                books.get(0).id.equals(UUID.fromString("0ae2a56c-5dd4-47ed-b230-690c11a786be"))
                        ));

    }

    @Test
    public void findBookByIllegalGenre() {
        final BookRepository bookRepository = app.injector().instanceOf(BookRepository.class);
        // Illegal genre id, must return empty list.
        final CompletionStage<List<Book>> stage
                = bookRepository.byGenre("f022920c-cb84-4981-9184-46b6f9a17de8", 0, 10);

        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(books ->
                                books.size() == 0
                        ));

    }

    @Test
    public void saveBook() {
        final BookRepository bookRepository = app.injector().instanceOf(BookRepository.class);
        final Book newBook = new Book();
        newBook.name = "The Hunting of the Snark";
        newBook.author = new Author();
        newBook.author.id = UUID.fromString("bdd6741d-71b9-493c-9c31-38af19b9e27c");
        newBook.year = 1876;
        newBook.edition = 1;
        newBook.genreCollection = new HashSet<Genre>() {{
            final Genre genre = new Genre();
            genre.id = UUID.fromString("f022820c-cb84-4981-9184-46b6f9a17de8");
        }};


        final CompletionStage<F.Either<String, Book>> stage = bookRepository.save(newBook);
        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(book ->
                                book.right.isPresent()
                                && !book.left.isPresent()
                                && Objects.nonNull(book.right.get().id)
                        ));
    }

    @Test
    public void saveBookFailed() {
        final BookRepository bookRepository = app.injector().instanceOf(BookRepository.class);
        final Book newBook = new Book();
        newBook.name = "The Hunting of the Snark";
        newBook.author = new Author();
        // Illegal author id
        newBook.author.id = UUID.fromString("bdd6741d-71b9-403c-9c31-38af19b9e27c");
        newBook.year = 1876;
        newBook.edition = 1;
        newBook.genreCollection = new HashSet<Genre>() {{
            final Genre genre = new Genre();
            genre.id = UUID.fromString("f022820c-cb84-4981-9184-46b6f9a17de8");
        }};


        final CompletionStage<F.Either<String, Book>> stage = bookRepository.save(newBook);
        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(book ->
                                !book.right.isPresent()
                                && book.left.isPresent()
                                && book.left.get().equals("book.author")
                        ));
    }

    @Test
    public void updateAuthor() {

        final AuthorRepository authorRepository = app.injector().instanceOf(AuthorRepository.class);

        final Author author = new Author();
        author.id = UUID.fromString("bdd6741d-71b9-493c-9c31-38af19b9e27c");
        author.firstName = "Lewis";
        author.middleName = "Lutwidge";
        author.lastName = "Carroll";

        final CompletionStage<F.Either<String, Author>> stage = authorRepository.save(author);
        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(book ->
                                book.right.isPresent()
                                && !book.left.isPresent()
                                && Objects.nonNull(book.right.get().id)
                        ));
    }

    @Test
    public void updateOutdatedAuthor() {
        final AuthorRepository authorRepository = app.injector().instanceOf(AuthorRepository.class);

        final Author author = new Author();
        author.id = UUID.fromString("bdd6741d-71b9-493c-9c31-38af19b9e27c");
        author.firstName = "Lewis";
        author.middleName = "Lutwidge";
        author.lastName = "Carroll";
        author.version = 10;

        final CompletionStage<F.Either<String, Author>> stage = authorRepository.save(author);
        await().until(() ->
                assertThat(stage.toCompletableFuture())
                        .isCompletedWithValueMatching(book ->
                                !book.right.isPresent()
                                && book.left.isPresent()
                                && book.left.get().equals("author.version")
                        ));
    }
}
