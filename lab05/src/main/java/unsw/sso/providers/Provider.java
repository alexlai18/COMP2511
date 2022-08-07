package unsw.sso.providers;
import java.util.Map;

import unsw.sso.ClientApp;
import unsw.sso.Token;

public interface Provider {
    public Token generateFormSubmission(String email, String password);
    public void addUser(String email, String password);
    public boolean getUserLockStatus(String email);
    public boolean doesUserExist(String email);
    public void lockUserUniversally(ClientApp app, String email);
    public void lockUser(String email);
    public int getLoginAttempts(String email);
    public void addLoginAttempt(String email);
    public Map<String, Boolean> getUserLockStatus();
    public void setUserLockStatus(Map<String, Boolean> userLockStatus);
}
