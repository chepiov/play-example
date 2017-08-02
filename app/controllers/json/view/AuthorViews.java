package controllers.json.view;

/**
 * Views for author entity response.
 */
public class AuthorViews {

    /**
     * Public view of author.
     */
    public interface Public extends NamedViews.Public {
    }

    /**
     * Internal view of author.
     */
    public interface Internal extends NamedViews.Internal, Public {
    }
}
