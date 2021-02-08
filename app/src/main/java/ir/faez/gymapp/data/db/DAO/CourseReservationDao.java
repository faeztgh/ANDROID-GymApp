package ir.faez.gymapp.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.gymapp.data.model.CourseReservation;

@Dao
public interface CourseReservationDao {

    @Insert
    long insert(CourseReservation courseReservation);

    @Update
    int update(CourseReservation courseReservation);

    @Query("DELETE FROM courseReservation where id= :id")
    int delete(String id);

    @Query("SELECT * FROM courseReservation WHERE ownerId= :id")
    List<CourseReservation> getAllcourseReservations(String id);
}
