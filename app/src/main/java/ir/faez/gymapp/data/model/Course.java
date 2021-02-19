package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity
public class Course implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    @SerializedName("title")
    private String courseTitle;
    @ColumnInfo
    @SerializedName("description")
    private String courseDesc;
    @ColumnInfo
    private double price;
    @ColumnInfo
    @SerializedName("poster")
    private String posterUrl;
    @ColumnInfo
    @SerializedName("video")
    private String videoUrl;
    @ColumnInfo
    private String dateTime;

    public Course(@NonNull String id, String courseTitle, String courseDesc, double price, String posterUrl, String videoUrl, String dateTime) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.price = price;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
        this.dateTime = dateTime;
    }

    @Ignore
    public Course(String courseTitle, String courseDesc, double price, String posterUrl, String videoUrl, String dateTime) {
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.price = price;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
        this.dateTime = dateTime;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getCourseDesc() {
        return courseDesc;
    }

    public double getPrice() {
        return price;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

}