/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restservice.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AMore
 */
public class UserStorage {

    private List<User> userStorage = new ArrayList<>();
    private static UserStorage instance = null;

    protected UserStorage() {
    }

    public static UserStorage getInstance() {
        if (instance == null) {
            instance = new UserStorage();
        }

        return instance;
    }

    public List<User> getUserStorage() {
        if (userStorage.isEmpty()) {
            userStorage.add(new User("andre", "word"));
            userStorage.add(new User("johan", "pass"));
            userStorage.add(new User("test", "test"));
        }

        return userStorage;
    }

    public void setUserStorage(List<User> userStorage) {
        this.userStorage = userStorage;
    }

}
