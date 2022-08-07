package unsw.sso.pages;
import unsw.sso.ClientApp;
import unsw.sso.Browser;

public interface Page {
    // Abstract methods
    public void doAction(ClientApp app, Browser page, Object input);
    public void visit(ClientApp app, Browser page);
    public void back(Browser page);
    public String getStateName();
}
