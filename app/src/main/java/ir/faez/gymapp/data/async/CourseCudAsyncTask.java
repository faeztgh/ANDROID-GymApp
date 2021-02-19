package ir.faez.gymapp.data.async;


import android.content.Context;
import android.os.AsyncTask;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.CourseDao;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.utils.Action;

public class CourseCudAsyncTask extends AsyncTask<Course, Void, Long> {
    private DbResponse<Course> dbResponse;
    private CourseDao courseDao;
    private Context context;
    private Course course;
    private String action;

    public CourseCudAsyncTask(Context context, String action, DbResponse<Course> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        courseDao = dbManager.courseDao();
    }

    @Override
    protected Long doInBackground(Course... courses) {
        course = courses[0];

        switch (action) {
            case Action.INSERT_ACTION:

                return insertDoInBackground(courses);

            case Action.UPDATE_ACTION:

                return updateDoInBackground(courses);

            case Action.DELETE_ACTION:

                return deleteDoInBackground(course);
        }
        return null;
    }

    private Long deleteDoInBackground(Course courses) {
        return (long) courseDao.delete(course.getId());
    }

    private Long updateDoInBackground(Course[] expenses) {
        return (long) courseDao.update(course);
    }

    private Long insertDoInBackground(Course[] courses) {
        return courseDao.insert(course);
    }

    @Override
    protected void onPostExecute(Long response) {

        switch (action) {
            case Action.INSERT_ACTION:
                insertPostExecute(response);
                break;
            case Action.UPDATE_ACTION:
                updatePostExecute(response);
                break;
            case Action.DELETE_ACTION:
                deletePostExecute(response);
                break;
        }
    }

    private void deletePostExecute(Long response) {
        if (response > 0) {
            dbResponse.onSuccess(course);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnDelete));
            dbResponse.onError(error);
        }

    }

    private void updatePostExecute(Long affectedRows) {
        if (affectedRows > 0) {
            dbResponse.onSuccess(course);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnUpdate));
            dbResponse.onError(error);
        }
    }

    private void insertPostExecute(Long courseId) {

        if (courseId > 0) {
            course.setId(courseId.toString());
            dbResponse.onSuccess(course);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnInsert));
            dbResponse.onError(error);
        }
    }
}
