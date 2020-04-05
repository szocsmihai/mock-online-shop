package ro.iteahome.shop.ui.options;

public class UIOption {

    private final String OPTION_NUMBER;
    private final String OPTION_DESCRIPTION;
    private final Action ACTION;

    public UIOption(String OPTION_NUMBER, String OPTION_DESCRIPTION, Action ACTION) {
        this.OPTION_NUMBER = OPTION_NUMBER;
        this.OPTION_DESCRIPTION = OPTION_DESCRIPTION;
        this.ACTION = ACTION;
    }

    public String getOPTION_NUMBER() {
        return OPTION_NUMBER;
    }

    public String getOPTION_DESCRIPTION() {
        return OPTION_DESCRIPTION;
    }

    public Action getACTION() {
        return ACTION;
    }

    public interface Action {
        void execute();
    }
}