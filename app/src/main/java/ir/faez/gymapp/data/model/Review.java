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
    private String dateTime;
    @ColumnInfo
    private String text;
    @ColumnInfo
    private String courseId;
    @ColumnInfo
    private String ownerId;
    @ColumnInfo
    private String ownerFullName;


    public Review(@NonNull String id, String dateTime, String text, String courseId, String ownerId, String ownerFullName) {
        this.id = id;
        this.dateTime = dateTime;
        this.text = text;
        this.courseId = courseId;
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
    }

    @Ignore
    public Review(String dateTime, String text, String courseId, String ownerId, String ownerFullName) {
        this.dateTime = dateTime;
        this.text = text;
        this.courseId = courseId;
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
    }


    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
