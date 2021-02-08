package ir.faez.gymapp.data;

import android.app.Application;
import android.util.Log;

import ir.faez.gymapp.data.model.User;

public class AppData extends Application {
    private static final String TAG = "AppData";
    private User currentUser = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        Log.d(TAG, "Current user set to: " + this.currentUser);

    }
}
