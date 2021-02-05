package ir.faez.gymapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    @ColumnInfo
    private String courseTitle;
    @ColumnInfo
    private String courseDesc;
    //Course video
    @ColumnInfo
    private List<Review> courseReviews;

}
