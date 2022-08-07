package unsw.sso.pages;
import unsw.sso.ClientApp;
import unsw.sso.Browser;
import unsw.sso.Token;

public class HomePage implements Page {
    @Override
    public void doAction(ClientApp app, Browser page, Object input) {
        // Do nothing
    }

    @Override
    public void visit(ClientApp app, Browser page) {
        // If it is at home page and it is reloaded, we need to check if cache is saved
        page.setPreviousState(null);
        for (Token t : page.getTokenCache()) {
            if (app.getCache().contains(t)) {
                page.setState(new HomePage());
                return;
            }
        }
        page.setState(new SelectPage());
    }

    @Override
    public void back(Browser page) {
        // Back button could either be null, or either LinkedOut or Hoogle Login
        // LinkedOut and Hoogle back button points to Select page
        if (page.getState() != null) {
            page.setState(page.getPreviousState());
            page.setPreviousState(new SelectPage());
        } else {
            page.setPreviousState(null);
            page.setState(null);
        }
    }

    @Override
    public String getStateName() {
        return "Home";
    }
}
