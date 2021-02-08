package ir.faez.gymapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.faez.gymapp.data.db.DAO.CourseDao;
import ir.faez.gymapp.data.db.DAO.CourseReservationDao;
import ir.faez.gymapp.data.db.DAO.ReviewDao;
import ir.faez.gymapp.data.db.DAO.UserDao;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.data.model.Review;
import ir.faez.gymapp.data.model.User;

@Database(entities = {User.class, Course.class, Review.class, CourseReservation.class}, version = 1, exportSchema = true)

public abstract class DbManager extends RoomDatabase {
    private static final String DB_NAME = "GymApp";
    private static DbManager dbManager;

    public static DbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = Room.databaseBuilder(context, DbManager.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return dbManager;
    }

    public abstract UserDao userDao();

    public abstract CourseDao courseDao();

    public abstract CourseReservationDao courseReservationDao();

    public abstract ReviewDao reviewDao();


}
