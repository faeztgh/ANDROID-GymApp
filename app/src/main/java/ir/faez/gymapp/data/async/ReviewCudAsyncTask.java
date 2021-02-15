package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.CourseReservationDao;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DAO.ReviewDao;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.Review;
import ir.faez.gymapp.data.model.Review;
import ir.faez.gymapp.utils.Action;

public class ReviewCudAsyncTask extends AsyncTask<Review, Void, Long> {
    private Context context;
    private ReviewDao reviewDao;
    private Review Review;
    private DbResponse<Review> dbResponse;
    private String action;

    public ReviewCudAsyncTask(Context context, String action, DbResponse<Review> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        reviewDao = dbManager.reviewDao();

    }

    @Override
    protected Long doInBackground(Review... reviews) {
        Review = reviews[0];

        switch (action) {
            case Action.INSERT_ACTION:

                return insertDoInBackground(reviews);

            case Action.UPDATE_ACTION:

                return updateDoInBackground(reviews);

            case Action.DELETE_ACTION:

                return deleteDoInBackground(reviews);
        }
        return null;
    }

    private Long deleteDoInBackground(Review[] Review) {
        return (long) reviewDao.delete(Review[0].getId());
    }

    private Long updateDoInBackground(Review[] reviews) {
        return (long) reviewDao.update(Review);
    }

    private Long insertDoInBackground(Review[] reviews) {
        return reviewDao.insert(Review);
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
            dbResponse.onSuccess(Review);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnDelete));
            dbResponse.onError(error);
        }

    }

    private void updatePostExecute(Long affectedRows) {
        if (affectedRows > 0) {
            dbResponse.onSuccess(Review);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnUpdate));
            dbResponse.onError(error);
        }
    }

    private void insertPostExecute(Long courseReservationId) {

        if (courseReservationId > 0) {
            Review.setId(courseReservationId.toString());
            dbResponse.onSuccess(Review);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnInsert));
            dbResponse.onError(error);
        }
    }
}
