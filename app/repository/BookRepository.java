package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.FetchConfig;
import io.ebean.Transaction;
import models.Author;
import models.Book;
import models.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.ebean.EbeanConfig;
import play.libs.F;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Book repository as service implementation.
 */
public class BookRepository implements SaveRepository<Book> {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepository.class);

    @Inject
    public BookRepository(final EbeanConfig ebeanConfig,
                          final DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }


    /**
     * Filters books by genre.
     *
     * @param genreId to use
     * @param offset  to use
     * @param max     to use
     * @return filtered books
     */
    public CompletionStage<List<Book>> byGenre(final String genreId, final int offset, final int max) {
        new FetchConfig().query();
        return supplyAsync(
                () -> ebeanServer.find(Book.class)
                        .where()
                        .eq("genreCollection.id", genreId)
                        .setFirstRow(offset)
                        .setMaxRows(max)
                        .findList(),
                executionContext);
    }

    @Override
    public CompletionStage<F.Either<String, Book>> save(final Book book) {
        return supplyAsync(
                () -> {
                    final Transaction txn = ebeanServer.beginTransaction();
                    try {

                        if (ebeanServer.find(Author.class).where()
                                .eq("id", book.author.id)
                                .findCount() != 1) {
                            LOGGER.warn("Author with id = {} not found", book.author.id);
                            return F.Either.Left("book.author");
                        }

                        if (ebeanServer.find(Genre.class)
                                .where()
                                .in("id", book.genreCollection.stream().map(g -> g.id).collect(Collectors.toSet()))
                                .findCount() != book.genreCollection.size()) {
                            LOGGER.warn("Some of the genres not found");
                            return F.Either.Left("book.genre");
                        }

                        if (Objects.nonNull(book.id)) {
                            if (ebeanServer.find(Book.class).where()
                                    .eq("id", book.id)
                                    .findCount() != 1) {
                                LOGGER.warn("Book with id = {} not found", book.id);
                                return F.Either.Left("book.id");
                            }
                            ebeanServer.update(book);
                        } else {
                            ebeanServer.save(book);
                        }
                        txn.commit();
                        return F.Either.Right(book);
                    } catch (OptimisticLockException e) {
                        LOGGER.warn("Old version saving");
                        txn.rollback();
                        return F.Either.Left("book.version");
                    } finally {
                        txn.end();
                    }
                },
                executionContext);
    }

}
