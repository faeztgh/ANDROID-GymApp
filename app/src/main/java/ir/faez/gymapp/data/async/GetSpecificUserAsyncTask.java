package ir.faez.gymapp.data.async;

import android.content.Context;
import android.os.AsyncTask;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DAO.UserDao;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.utils.Action;

public class GetSpecificUserAsyncTask extends AsyncTask<String, Void, User> {
    private Context context;
    private UserDao userDao;
    private DbResponse<User> dbResponse;
    private String action;

    public GetSpecificUserAsyncTask(Context context, String action, DbResponse<User> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        userDao = dbManager.userDao();
    }

    @Override
    protected User doInBackground(String... strings) {

        switch (action) {
            case Action.GET_BY_USERNAME_ACTION:
                return userDao.getUserByUsername(strings[0]);
            case Action.GET_BY_STATE_ACTION:
                return userDao.getUserByState(strings[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {

        switch (action) {
            case Action.GET_BY_STATE_ACTION:
            case Action.GET_BY_USERNAME_ACTION:
                if (user != null) {
                    dbResponse.onSuccess(user);
                } else {
                    Error error = new Error(String.valueOf(R.string.somethingWentWrong));
                    dbResponse.onError(error);
                }
                break;

        }
    }
}
