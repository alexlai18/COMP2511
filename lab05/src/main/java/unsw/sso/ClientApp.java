package unsw.sso;
import unsw.sso.providers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.List;


public class ClientApp {
    private List<Provider> providerList = new ArrayList<Provider>();
    private Map<String, List<String>> usersExist = new HashMap<>();
    private final String name;
    private List<Token> cache = new ArrayList<>();

    public ClientApp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Provider> getProviderList() {
        return providerList;
    }

    public Provider getProvider(String providerName) throws NoSuchElementException {
        for (Provider p : providerList) {
            if (p.getClass().getSimpleName() == providerName) {
                return p;
            }
        }
        throw new NoSuchElementException("Provider does not exist in app");
    }

    public List<String> getUserProviders(String email) {
        return this.usersExist.get(email);
    }

    
    public void registerProvider(Object o) {
        // This lock should be updated constantly, so only 1st provider needs to be taken
        if (!providerList.contains(o) && (o instanceof Provider)) {
            if (providerList.size() > 0) {
                transferLockStatusToNew((Provider) o);
                transferLockStatusToOld((Provider) o);
            }
            providerList.add((Provider) o);
        }

    }

    public void transferLockStatusToNew(Provider o) {
        Provider p = providerList.get(0);
        for (Map.Entry<String, Boolean> entry : p.getUserLockStatus().entrySet()) {
            if (entry.getValue()) {
                ((Provider) o).lockUser(entry.getKey());
            } 
        }
    }

    public void transferLockStatusToOld(Provider o) {
        for (Map.Entry<String, Boolean> entry : o.getUserLockStatus().entrySet()) {
            for (Provider p : providerList) {
                if (entry.getValue()) {
                    p.lockUser(entry.getKey());
                }
            }
        }
    }

    public boolean hasProvider(Object provider) {
        return providerList.contains(provider);
    }

    public void registerUser(Token token) {
        // Adds new provider to the provider list if the email is already input
        if (usersExist.containsKey(token.getUserEmail())) {
            usersExist.get(token.getUserEmail()).add(token.getProviderName());
        } else {
            List<String> providers = new ArrayList<String>(Arrays.asList(token.getProviderName()));
            usersExist.put(token.getUserEmail(), providers);
        }
        addCache(token);
    }

    public boolean hasUserForProvider(String email, Provider provider) {
        return hasProvider(provider) && provider.doesUserExist(email);
    }

    public void addCache(Token t) {
        cache.add(t);
    }

    public List<Token> getCache() {
        return this.cache;
    }
}
