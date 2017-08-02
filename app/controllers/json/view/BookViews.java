package controllers.json.view;

/**
 * Views for book entity response.
 */
public class BookViews {

    /**
     * Public view of book.
     */
    public interface Public extends NamedViews.Public {
    }

    /**
     * Internal view of book.
     */
    public interface Internal extends NamedViews.Internal, Public {
    }
}
