package controllers.json.parser;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import models.Book;
import play.libs.F;
import play.libs.streams.Accumulator;
import play.mvc.BodyParser;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.Executor;

/**
 * Book entity body parser.
 */
public class BookBodyParser implements BodyParser<Book> {

    private final BodyParser.Json jsonParser;
    private final Executor executor;

    @Inject
    public BookBodyParser(final BodyParser.Json jsonParser, final Executor executor) {
        this.jsonParser = jsonParser;
        this.executor = executor;
    }

    @Override
    public Accumulator<ByteString, F.Either<Result, Book>> apply(final RequestHeader request) {
        final Accumulator<ByteString, F.Either<Result, JsonNode>> jsonAccumulator = jsonParser.apply(request);
        return jsonAccumulator.map(resultOrJson -> {
            if (resultOrJson.left.isPresent()) {
                return F.Either.Left(resultOrJson.left.get());
            } else {
                //noinspection ConstantConditions
                final JsonNode json = resultOrJson.right.get();
                try {
                    final Book book = play.libs.Json.fromJson(json, Book.class);
                    return F.Either.Right(book);
                } catch (Exception e) {
                    return F.Either.Left(Results.badRequest(
                            play.libs.Json.newObject().putPOJO(
                                    "messages",
                                    new String[]{"Unable to read Book from json"}
                            )));
                }
            }
        }, executor);
    }
}
