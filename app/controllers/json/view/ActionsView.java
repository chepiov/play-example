package controllers.json.view;

/**
 * Views for action response.
 */
public class ActionsView {
    /**
     * View for creation actions.
     */
    public interface CreateView {
    }

    /**
     * View for update actions.
     */
    public interface UpdateView extends CreateView {
    }
}
