package controllers;

import controllers.json.JsonEx;
import controllers.json.parser.AuthorBodyParser;
import controllers.json.parser.BookBodyParser;
import controllers.json.view.ActionsView;
import controllers.json.view.BookViews;
import models.Author;
import models.Book;
import models.NamedModel;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.SaveRepository;
import views.html.index;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;
import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * API implementation.
 */
public class ApiController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final MessagesApi messagesApi;
    private final Validator validator;

    @Inject
    public ApiController(final BookRepository bookRepository,
                         final AuthorRepository authorRepository,
                         final MessagesApi messagesApi,
                         final Validator validator) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.messagesApi = messagesApi;
        this.validator = validator;
    }

    /**
     * Returns books filtered by genre.
     *
     * @param genreId  to filter
     * @param offset   to use
     * @param max      to use
     * @param internal show internal state of books
     * @return filtered books
     */
    public CompletionStage<Result> byGenre(final String genreId,
                                           final int offset,
                                           final int max,
                                           final boolean internal) {

        return bookRepository.byGenre(genreId, offset, max)
                .thenApplyAsync(books ->
                        ok(JsonEx.toJson(internal ? BookViews.Internal.class : BookViews.Public.class, books)));
    }

    /**
     * Creates book.
     *
     * @return info about saved book or validation/saving error messages
     */
    @BodyParser.Of(BookBodyParser.class)
    public CompletionStage<Result> createBook() {
        final Book book = request().body().as(Book.class);
        return save(book, null, bookRepository);
    }

    /**
     * Updates book.
     *
     * @param id contains id of book to update
     * @return info about saved book or validation/saving error messages
     */
    public CompletionStage<Result> updateBook(final String id) {
        final Book book = request().body().as(Book.class);
        return save(book, id, bookRepository);
    }

    /**
     * Creates author.
     *
     * @return info about saved author or validation/saving error messages
     */
    @BodyParser.Of(AuthorBodyParser.class)
    public CompletionStage<Result> createAuthor() {
        final Author author = request().body().as(Author.class);
        return save(author, null, authorRepository);
    }

    /**
     * Updates author.
     *
     * @param id contains id of author to update
     * @return info about saved author or validation/saving error messages
     */
    @BodyParser.Of(AuthorBodyParser.class)
    public CompletionStage<Result> updateAuthor(final String id) {
        final Author author = request().body().as(Author.class);
        return save(author, id, authorRepository);
    }

    /**
     * Simple index page
     */
    public Result index() {
        return ok(index.render());
    }

    private <T extends NamedModel> CompletionStage<Result> save(T entity, String id, SaveRepository<T> repository) {

        if (Objects.nonNull(id)) {
            entity.id = UUID.fromString(id);
        } else {
            entity.id = null;
        }

        final Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            final List<String> messages =
                    violations.stream().map(v -> getMessage(v.getMessage())).collect(toList());
            return CompletableFuture.completedFuture(
                    badRequest(Json.newObject().putPOJO("messages", messages)));
        }

        return repository.save(entity)
                .thenApplyAsync(result -> {
                    if (result.left.isPresent()) {
                        return badRequest(Json.newObject().put("message", result.left.get()));
                    } else {
                        if (Objects.nonNull(id)) {
                            return ok(JsonEx.toJson(ActionsView.UpdateView.class, result.right));
                        } else {
                            return ok(JsonEx.toJson(ActionsView.CreateView.class, result.right));
                        }
                    }
                });
    }

    private String getMessage(final String code, final Object... args) {
        return messagesApi.preferred(request()).at(code, args);
    }
}
