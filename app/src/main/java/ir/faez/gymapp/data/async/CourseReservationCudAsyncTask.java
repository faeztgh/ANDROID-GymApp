package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.CourseReservationDao;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.utils.Action;

public class CourseReservationCudAsyncTask extends AsyncTask<CourseReservation, Void, Long> {
    private CourseReservationDao courseReservationDao;
    private DbResponse<CourseReservation> dbResponse;
    private CourseReservation courseReservation;
    private Context context;
    private String action;

    public CourseReservationCudAsyncTask(Context context, String action,
                                         DbResponse<CourseReservation> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        courseReservationDao = dbManager.courseReservationDao();
    }

    @Override
    protected Long doInBackground(CourseReservation... courseReservations) {
        courseReservation = courseReservations[0];

        switch (action) {
            case Action.INSERT_ACTION:

                return insertDoInBackground(courseReservations);

            case Action.UPDATE_ACTION:

                return updateDoInBackground(courseReservations);

            case Action.DELETE_ACTION:

                return deleteDoInBackground(courseReservations);
        }
        return null;
    }

    private Long deleteDoInBackground(CourseReservation[] courseReservation) {
        return (long) courseReservationDao.delete(courseReservation[0].getCourseId());
    }

    private Long updateDoInBackground(CourseReservation[] courseReservations) {
        return (long) courseReservationDao.update(courseReservation);
    }

    private Long insertDoInBackground(CourseReservation[] courseReservations) {
        return courseReservationDao.insert(courseReservation);
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
            dbResponse.onSuccess(courseReservation);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnDelete));
            dbResponse.onError(error);
        }

    }

    private void updatePostExecute(Long affectedRows) {
        if (affectedRows > 0) {
            dbResponse.onSuccess(courseReservation);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnUpdate));
            dbResponse.onError(error);
        }
    }

    private void insertPostExecute(Long courseReservationId) {

        if (courseReservationId > 0) {
            courseReservation.setId(courseReservationId.toString());
            dbResponse.onSuccess(courseReservation);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnInsert));
            dbResponse.onError(error);
        }
    }
}
