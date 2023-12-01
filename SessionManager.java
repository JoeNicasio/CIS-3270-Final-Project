// SessionManager.java
package application;

public class SessionManager {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    // Use this method to get the ID of the currently logged-in user
    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getId();
        } else {
            throw new IllegalStateException("No user is currently logged in.");
        }
    }
    
    // Use this method to get the first name of the currently logged-in user
    public static String getCurrentUserFirstName() {
        if (currentUser != null) {
            return currentUser.getFirstName();
        } else {
            throw new IllegalStateException("No user is currently logged in.");
        }
    }
}
