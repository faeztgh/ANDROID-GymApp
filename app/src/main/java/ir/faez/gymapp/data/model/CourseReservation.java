package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class CourseReservation implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String reservationCode;
    @ColumnInfo
    private String status;
    @ColumnInfo
    private String courseId;
    @ColumnInfo
    private String ownerId;

    public CourseReservation(@NonNull String id, String reservationCode, String status, String courseId, String ownerId) {
        this.id = id;
        this.reservationCode = reservationCode;
        this.status = status;
        this.courseId = courseId;
        this.ownerId = ownerId;
    }

    @Ignore
    public CourseReservation(String reservationCode, String status, String courseId, String ownerId) {
        this.reservationCode = reservationCode;
        this.status = status;
        this.courseId = courseId;
        this.ownerId = ownerId;
    }

    @Ignore
    public CourseReservation(String status, String courseId, String ownerId) {
        this.status = status;
        this.courseId = courseId;
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

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
