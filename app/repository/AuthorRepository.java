package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.ebean.EbeanConfig;
import play.libs.F;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Author repository as service implementation.
 */
public class AuthorRepository implements SaveRepository<Author> {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepository.class);

    @Inject
    public AuthorRepository(final EbeanConfig ebeanConfig,
                            final DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<F.Either<String, Author>> save(final Author author) {
        return supplyAsync(
                () -> {
                    final Transaction txn = ebeanServer.beginTransaction();
                    try {
                        if (Objects.nonNull(author.id)) {
                            if (ebeanServer.find(Author.class).where()
                                    .eq("id", author.id)
                                    .findCount() != 1) {
                                LOGGER.warn("Author with id = {} not found", author.id);
                                return F.Either.Left("author.id");
                            }
                            ebeanServer.update(author);
                        } else {
                            ebeanServer.save(author);
                        }
                        txn.commit();
                        return F.Either.Right(author);
                    } catch (OptimisticLockException e) {
                        LOGGER.warn("Old version saving");
                        txn.rollback();
                        return F.Either.Left("author.version");
                    } finally {
                        txn.end();
                    }
                },
                executionContext);
    }
}
