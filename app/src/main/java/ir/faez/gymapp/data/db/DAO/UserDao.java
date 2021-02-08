package ir.faez.gymapp.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.gymapp.data.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Update
    int update(User user);

    @Query("DELETE FROM user WHERE id= :id")
    int delete(String id);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE userName= :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM user WHERE isLoggedIn= :state")
    User getUserByState(String state);

}
