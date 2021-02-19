package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

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
    @ColumnInfo
    @TypeConverters({ir.faez.gymapp.data.model.TypeConverters.class})
    @SerializedName("createdAt")
    private Date reservedDate;


    public CourseReservation(@NonNull String id, String reservationCode, String status, String courseId, String ownerId, Date reservedDate) {
        this.id = id;
        this.reservationCode = reservationCode;
        this.status = status;
        this.courseId = courseId;
        this.ownerId = ownerId;
        this.reservedDate = reservedDate;
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

    public Date getReservedDate() {
        return reservedDate;
    }

    public String getOwnerId() {
        return ownerId;
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

}
