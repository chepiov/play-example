package controllers.json.view;

/**
 * Views for genre entity response.
 */
public class GenreViews {

    /**
     * Public view of genre.
     */
    public interface Public extends NamedViews.Public {
    }

    /**
     * Internal view of genre.
     */
    public interface Internal extends NamedViews.Internal, Public {
    }

}
