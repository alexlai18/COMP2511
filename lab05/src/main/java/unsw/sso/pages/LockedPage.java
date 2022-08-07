package unsw.sso.pages;
import unsw.sso.ClientApp;
import unsw.sso.Browser;

public class LockedPage implements Page {
    
    public void doAction(ClientApp app, Browser page, Object input) {
        // There is no action that can be done when locked (unless visit is called again)
        return;
    }
    
    public void visit(ClientApp app, Browser page) {
        // If it is on locked page and visited, it means that it has no cache loaded in
        page.setPreviousState(page.getState());
        page.setState(new SelectPage());
    }
    
    
    public void back(Browser page) {
        page.setPreviousState(null);
        page.setState(new SelectPage());
    }
    
    public String getStateName() {
        return "Locked";
    }
}
