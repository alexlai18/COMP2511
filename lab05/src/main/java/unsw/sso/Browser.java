package unsw.sso;
import unsw.sso.pages.*;

import unsw.sso.providers.Provider;
import java.util.ArrayList;
import java.util.List;

public class Browser {
    private List<Token> tokenCache = new ArrayList<>();
    private Page currentPage = null;
    private Page previousPage = null;
    private ClientApp currentApp = null;

    public void visit(ClientApp app) {
        // Checking cache
        for (Token t : getTokenCache()) {
            if (app.getCache().contains(t)) {
                if (checkCacheLocked(t, app)) {
                    return;
                }
                setPreviousState(null);
                setState(new HomePage());
                return;
            }
        }

        if (currentPage == null) {
            setState(new SelectPage());
        } else {
            currentPage.visit(app, this);
        }
        currentApp = app;
    }

    public Page getState() {
        return this.currentPage;
    }

    public Page getPreviousState() {
        return this.previousPage;
    }

    public void setState(Page newPage) {
        this.currentPage = newPage;
    }

    public void setPreviousState(Page newPage) {
        this.previousPage = newPage;
    }

    public String getCurrentPageName() {
        if (currentPage == null) {
            return null;
        }
        return this.currentPage.getStateName();
    }

    public void clearCache() {
        tokenCache = new ArrayList<>();
        // Clears history so previousPage doesn't exist
        // Current page remains until page reloads
        previousPage = null;
    }

    public List<Token> getTokenCache() {
        return this.tokenCache;
    }

    public void registerUser(Token token) {
        currentApp.registerUser(token);
    }

    public void addTokenCache(Token token) {
        this.tokenCache.add(token);
    }

    public void interact(Object using) {
        if (using == null) {
            currentPage.back(this);
            return;
        }

        currentPage.doAction(currentApp, this, using);
    }

    public Boolean checkCacheLocked(Token t, ClientApp app) {
        String email = t.getUserEmail();
        for (Provider p : app.getProviderList()) {
            if (p.getUserLockStatus(email)) {
                setPreviousState(new SelectPage());
                setState(new LockedPage());
                return true;
            }
        }
        return false;
    }
}
