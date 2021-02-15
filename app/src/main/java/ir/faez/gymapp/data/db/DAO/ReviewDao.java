package ir.faez.gymapp.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.gymapp.data.model.Review;

@Dao
public interface ReviewDao {
    @Insert
    long insert(Review review);

    @Update
    int update(Review review);

    @Query("DELETE FROM review where id= :id")
    int delete(String id);

    @Query("SELECT * FROM review WHERE courseId= :id")
    List<Review> getAllReviews(String id);
}
