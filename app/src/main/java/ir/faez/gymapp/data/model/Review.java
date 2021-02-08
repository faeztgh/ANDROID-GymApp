package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Review {
    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String reviewDate;
    @ColumnInfo
    private String reviewTime;
    @ColumnInfo
    private String reviewText;
    @ColumnInfo
    private String reviewOwner;
    @ColumnInfo
    private String reviewCourse;
    @ColumnInfo
    private String ownerId;

    public Review(@NonNull String id, String reviewDate, String reviewTime, String reviewText, String reviewOwner, String reviewCourse, String ownerId) {
        this.id = id;
        this.reviewDate = reviewDate;
        this.reviewTime = reviewTime;
        this.reviewText = reviewText;
        this.reviewOwner = reviewOwner;
        this.reviewCourse = reviewCourse;
        this.ownerId = ownerId;
    }

    @Ignore
    public Review(String reviewDate, String reviewTime, String reviewText, String reviewOwner, String reviewCourse,String ownerId) {
        this.reviewDate = reviewDate;
        this.reviewTime = reviewTime;
        this.reviewText = reviewText;
        this.reviewOwner = reviewOwner;
        this.reviewCourse = reviewCourse;
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

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewOwner() {
        return reviewOwner;
    }

    public void setReviewOwner(String reviewOwner) {
        this.reviewOwner = reviewOwner;
    }

    public String getReviewCourse() {
        return reviewCourse;
    }

    public void setReviewCourse(String reviewCourse) {
        this.reviewCourse = reviewCourse;
    }
}
