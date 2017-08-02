package repository;

import models.NamedModel;
import play.libs.F;

import java.util.concurrent.CompletionStage;

/**
 * Repository (more precisely service in the usual sense) which can save (create or update) entity.
 *
 * @param <T> entity type to save
 */
public interface SaveRepository<T extends NamedModel> {
    /**
     * Saves (creates or updates) the entity.
     *
     * @param entity to save
     * @return saved entity in case of success, otherwise error message
     */
    CompletionStage<F.Either<String, T>> save(final T entity);
}
