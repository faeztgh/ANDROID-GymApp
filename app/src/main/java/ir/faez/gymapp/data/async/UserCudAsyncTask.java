package ir.faez.gymapp.data.async;


import android.content.Context;
import android.os.AsyncTask;

import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.db.DAO.UserDao;
import ir.faez.gymapp.data.db.DbManager;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.utils.Action;

public class UserCudAsyncTask extends AsyncTask<User, Void, Long> {
    private Context context;
    private UserDao userDao;
    private User user;
    private DbResponse<User> dbResponse;
    private String action;

    public UserCudAsyncTask(Context context, String action, DbResponse<User> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        userDao = dbManager.userDao();
    }

    @Override
    protected Long doInBackground(User... users) {
        user = users[0];


        switch (action) {
            case Action.INSERT_ACTION:

                return insertDoInBackground(users);

            case Action.UPDATE_ACTION:

                return updateDoInBackground(users);

            case Action.DELETE_ACTION:

                return deleteDoInBackground(users);

        }
        return null;
    }


    private Long deleteDoInBackground(User[] users) {
        return (long) userDao.delete(user.getId());
    }

    private Long updateDoInBackground(User[] users) {
        return (long) userDao.update(user);
    }

    private Long insertDoInBackground(User[] users) {
        return userDao.insert(user);
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
            dbResponse.onSuccess(user);
        } else {
            Error error = new Error("Something went wrong on delete :(");
            dbResponse.onError(error);
        }
    }

    private void updatePostExecute(Long affectedRows) {
        if (affectedRows > 0) {
            dbResponse.onSuccess(user);
        } else {
            Error error = new Error("Something went wrong on update :(");
            dbResponse.onError(error);
        }
    }

    private void insertPostExecute(Long userId) {
        if (userId > 0) {
            user.setId(userId.toString());
            dbResponse.onSuccess(user);
        } else {
            Error error = new Error("Something went wrong on insert :(");
            dbResponse.onError(error);
        }
    }
}

