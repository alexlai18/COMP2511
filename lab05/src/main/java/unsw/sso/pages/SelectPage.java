package unsw.sso.pages;
import unsw.sso.ClientApp;
import unsw.sso.Browser;
import unsw.sso.providers.*;

public class SelectPage implements Page {
    @Override
    public void doAction(ClientApp app, Browser page, Object input) {
        if (!(input instanceof Provider)) {
            // Do nothing
            return;
        }

        // We can safely cast after confirming type
        Provider provider = (Provider) input;

        // Selects a provider
        if (provider instanceof LinkedOut) {
            page.setPreviousState(this);
            page.setState(new LinkedOutLoginPage());
        } else {
            page.setPreviousState(this);
            page.setState(new HoogleLoginPage());
        }
    }

    @Override
    public void visit(ClientApp app, Browser page) {
        // This does nothing as if you were on this page, no cache is stored anyway
        page.setPreviousState(null);
        page.setState(new SelectPage());
    }

    @Override
    public void back(Browser page) {
        page.setPreviousState(null);
        page.setState(null);
    }

    @Override
    public String getStateName() {
        return "Select a Provider";
    }
}
