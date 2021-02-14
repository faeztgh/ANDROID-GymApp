package ir.faez.gymapp.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.gymapp.data.model.Course;

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Course course);

    @Update
    int update(Course course);

    @Query("DELETE FROM course where id= :id")
    int delete(String id);

    @Query("SELECT * FROM course")
    List<Course> getAllCourses();
}
