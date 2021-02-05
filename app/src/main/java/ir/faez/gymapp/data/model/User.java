package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String fullName;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String password;
    @ColumnInfo
    private String sessionToken;
    @ColumnInfo
    private List<String> courses;
    private String isLoggedIn;

    public User(String id, String fullName, String email, String username, String password, List<String> courses) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.courses = courses;
    }

    @Ignore
    public User(String fullName, String email, String username, String password) {

        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;


    }


    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(String isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
