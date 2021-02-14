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

    public Course(@NonNull String id, String courseTitle, String courseDesc, double price, String posterUrl, String videoUrl) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.price = price;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
    }


    @Ignore
    public Course(String courseTitle, String courseDesc, double price, String posterUrl, String videoUrl) {
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.price = price;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
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


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}