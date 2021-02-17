package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.CourseReservationDao;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.utils.Action;

public class GetSpecificCourseReservationAsyncTask extends AsyncTask<String, Void, List<CourseReservation>> {
    private Context context;
    private CourseReservationDao userDao;
    private DbResponse<List<CourseReservation>> dbResponse;
    private String action;

    public GetSpecificCourseReservationAsyncTask(Context context, String action, DbResponse<List<CourseReservation>> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        userDao = dbManager.courseReservationDao();
    }

    @Override
    protected List<CourseReservation> doInBackground(String... strings) {

        switch (action) {
            case Action.GET_BY_COURSE_RESERVATION_COURSE_ID:
                return userDao.getCourseReservationByCourseId(strings[0]);
            case Action.GET_BY_COURSE_RESERVATION_OWNER_ID:
                return userDao.getCourseReservationByOwnerId(strings[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<CourseReservation> courseReservations) {

        switch (action) {
            case Action.GET_BY_COURSE_RESERVATION_COURSE_ID:
            case Action.GET_BY_COURSE_RESERVATION_OWNER_ID:
                if (courseReservations != null) {
                    dbResponse.onSuccess(courseReservations);
                } else {
                    Error error = new Error(String.valueOf(R.string.somethingWentWrong));
                    dbResponse.onError(error);
                }
                break;

        }
    }
}
