package ir.faez.gymapp.data;

import android.app.Application;
import android.util.Log;

import java.util.List;

import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.data.model.User;

public class AppData extends Application {
    private static final String TAG = "AppData";
    private User currentUser = null;
    private List<Course> allCourses = null;
    private List<CourseReservation> allCourseReservations = null;

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
