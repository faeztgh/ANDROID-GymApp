package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Course {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String courseTitle;
    @ColumnInfo
    private String courseDesc;
    @ColumnInfo
    private String ownerId;


    // ADD Course video Field @TODO


    public Course(@NonNull String id, String courseTitle, String courseDesc, String ownerId) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.ownerId = ownerId;

    }

    @Ignore
    public Course(String courseTitle, String courseDesc, String ownerId) {
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }
}