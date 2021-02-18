package ir.faez.gymapp.data;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.data.model.User;

public class AppData extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    private static final String TAG = "AppData";
    private User currentUser = null;
    private List<Course> allCourses = null;
    private List<CourseReservation> allCourseReservations = null;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        Log.d(TAG, "Current user set to: " + this.currentUser);
    }

    public List<CourseReservation> getAllCourseReservations() {
        return allCourseReservations;
    }

    public void setAllCourseReservations(List<CourseReservation> allCourseReservations) {
        this.allCourseReservations = allCourseReservations;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(List<Course> allCourses) {
        this.allCourses = allCourses;
    }
}
