package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DAO.ReviewDao;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.Review;

public class GetReviewsAsyncTask extends AsyncTask<String, Void, List<Review>> {
    private Context context;
    private ReviewDao reviewDao;
    private DbResponse<List<Review>> dbResponse;

    public GetReviewsAsyncTask(Context context, DbResponse<List<Review>> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
    }

    @Override
    protected List<Review> doInBackground(String... id) {
        return reviewDao.getAllReviews(id[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        reviewDao = dbManager.reviewDao();

    }


    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);
        if (reviews != null) {
            dbResponse.onSuccess(reviews);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrong));
            dbResponse.onError(error);
        }
    }
}
