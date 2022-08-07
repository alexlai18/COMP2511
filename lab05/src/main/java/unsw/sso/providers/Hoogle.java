package unsw.sso.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import unsw.sso.ClientApp;
import unsw.sso.Token;

public class Hoogle implements Provider {
    private Map<String, String> userMappings = new HashMap<>();
    private Map<String, Boolean> userLockStatus = new HashMap<>();
    private Map<String, Integer> userLoginAttempts = new HashMap<>();

    public void addUser(String email, String password) {
        userMappings.put(email, password);
        userLockStatus.put(email, false);
        userLoginAttempts.put(email, 0);
    }

    public boolean getUserLockStatus(String email) {
        return userLockStatus.get(email);
    }

    public boolean doesUserExist(String email) {
        return userMappings.containsKey(email);
    }

    public void lockUserUniversally(ClientApp app, String email) {
        List<Provider> providerList = app.getProviderList();
        for (Provider p : providerList) {
            p.lockUser(email);
        }

    }

    public void lockUser(String email) {
        userLockStatus.put(email, true);
    }

    public int getLoginAttempts(String email) {
        return userLoginAttempts.get(email);
    }

    public void addLoginAttempt(String email) {
        userLoginAttempts.put(email, userLoginAttempts.get(email) + 1);
    }

    public Token generateFormSubmission(String email, String password) {
        if (Objects.equals(userMappings.get(email), password)) {
            return new Token(UUID.randomUUID().toString(), email, getClass().getSimpleName());
        } else {
            return new Token(null, email, getClass().getSimpleName());
        }
    }

    public Map<String, Boolean> getUserLockStatus() {
        return userLockStatus;
    }

    public void setUserLockStatus(Map<String, Boolean> userLockStatus) {
        this.userLockStatus = userLockStatus;
    }
}
