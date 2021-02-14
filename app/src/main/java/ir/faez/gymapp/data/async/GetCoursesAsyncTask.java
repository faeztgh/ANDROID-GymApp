package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.CourseDao;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.Course;

public class GetCoursesAsyncTask extends AsyncTask<String, Void, List<Course>> {
    private Context context;
    private CourseDao courseDao;
    private DbResponse<List<Course>> dbResponse;

    public GetCoursesAsyncTask(Context context, DbResponse<List<Course>> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
    }

    @Override
    protected List<Course> doInBackground(String... id) {
        return courseDao.getAllCourses();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        courseDao = dbManager.courseDao();

    }


    @Override
    protected void onPostExecute(List<Course> courses) {
        super.onPostExecute(courses);
        if (courses != null) {
            dbResponse.onSuccess(courses);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrong));
            dbResponse.onError(error);
        }
    }
}
