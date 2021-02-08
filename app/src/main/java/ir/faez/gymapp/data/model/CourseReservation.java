package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class CourseReservation {
    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String reservationCode;
    @ColumnInfo
    private String courseStatus;
    @ColumnInfo
    private String userId;
    @ColumnInfo
    private String courseId;
    @ColumnInfo
    private String ownerId;

    public CourseReservation(@NonNull String id, String reservationCode, String courseStatus, String userId, String courseId, String ownerId) {
        this.id = id;
        this.reservationCode = reservationCode;
        this.courseStatus = courseStatus;
        this.userId = userId;
        this.courseId = courseId;
        this.ownerId = ownerId;
    }

    @Ignore
    public CourseReservation(String reservationCode, String courseStatus, String userId, String courseId) {
        this.reservationCode = reservationCode;
        this.courseStatus = courseStatus;
        this.userId = userId;
        this.courseId = courseId;
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

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
