package unsw.sso.pages;
import unsw.sso.ClientApp;

import unsw.sso.Browser;
import unsw.sso.Token;
import unsw.sso.providers.Provider;


public class LinkedOutLoginPage implements Page {
    @Override
    public void doAction(ClientApp app, Browser page, Object input) {
        if (!(input instanceof Token)) {
            // Do nothing
            return;
        }
        
        // We can safely cast after confirming type
        Token userToken = (Token) input;
        // Find the amount of times a login has been attempted
        Provider LinkedOut = app.getProvider(userToken.getProviderName());
        String email = userToken.getUserEmail();

        if (!LinkedOut.doesUserExist(email)) {
            page.setState(new SelectPage());
            page.setPreviousState(null);
            return;
        }

        if (LinkedOut.getUserLockStatus(email)) {
            page.setPreviousState(new SelectPage());
            page.setState(new LockedPage());
            return;
        }

        // Logs into the provider, leads it to the home page
        if (userToken.getAccessToken() != null) {
            page.setPreviousState(page.getState());
            page.setState(new HomePage());
            page.registerUser(userToken);
            page.addTokenCache(userToken);
        } else {
            // If accessToken is null, then the user is not authenticated
            // If previous attempts were less than 2, go back to select page
            // If previous attempts >= 2, its too much, and account gets locked
            if (LinkedOut.getLoginAttempts(email) < 2) {
                page.setPreviousState(null);
                page.setState(new SelectPage());
                LinkedOut.addLoginAttempt(email);
                page.setPreviousState(null);
                page.setState(new SelectPage());
            } else {
                LinkedOut.addLoginAttempt(email);
                LinkedOut.lockUserUniversally(app, email);
                page.setPreviousState(new SelectPage());
                page.setState(new LockedPage());
            }
        }
    }
    
    @Override
    public void visit(ClientApp app, Browser page) {
        // If it is on login page and visited, it means that it has no cache loaded in
        page.setPreviousState(page.getState());
        page.setState(new SelectPage());
    }

    @Override
    public void back(Browser page) {
        // The login page always backs to select page -> which always backs to null
        page.setState(new SelectPage());
        page.setPreviousState(null);
    }

    @Override
    public String getStateName() {
        return "LinkedOut Login";
    }
}
