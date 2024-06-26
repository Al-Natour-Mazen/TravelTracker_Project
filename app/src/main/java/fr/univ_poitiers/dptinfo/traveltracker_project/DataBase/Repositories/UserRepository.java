package fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.DAOs.UserDao;
import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.UserRoomDatabase;
import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Entities.User;

/**
 * Repository class for handling user-related database operations.
 */
public class UserRepository {
    private final UserDao userDao;

    /**
     * Constructor for UserRepository.
     * @param application The application context.
     */
    public UserRepository(Application application) {
        UserRoomDatabase userRoomDatabase = UserRoomDatabase.getDatabase(application);
        userDao = userRoomDatabase.userDao();
    }

    /**
     * Retrieves a LiveData object containing a User entity based on provided username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return LiveData containing the User entity.
     */
    public LiveData<User> getUser(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    /**
     * Retrieves a LiveData object containing a User entity based on provided username.
     * @param username The username of the user.
     * @return LiveData containing the User entity.
     */
    public LiveData<User> getUser(String username) {
        return userDao.getUserByUsername(username);
    }

    /**
     * Creates a new user in the database.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User createUser(String firstname, String lastname, String username, String password) {
        User newUser = new User(firstname, lastname, username, password);
        insertUser(newUser);
        return newUser;
    }

    /**
     * Inserts a user entity into the database in a background thread.
     * @param user The User entity to insert.
     */
    private void insertUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> userDao.insert(user));
    }
}
