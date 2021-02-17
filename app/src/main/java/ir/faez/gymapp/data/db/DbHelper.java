package ir.faez.gymapp.data.db;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.CourseCudAsyncTask;
import ir.faez.gymapp.data.async.GetCoursesAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityAllCoursesBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.CourseAdapter;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;

public class DbHelper {
    private static final String TAG = "DB_HELPER";
    private static DbHelper instance = null;
    private Context context;


    private DbHelper(Context context) {
        this.context = context;

    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

}
