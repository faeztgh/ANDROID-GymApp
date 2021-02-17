package ir.faez.gymapp.data.db;

import android.content.Context;

import ir.faez.gymapp.network.NetworkHelper;

public class DbHelper {
    private static final String TAG = "DB_HELPER";
    private static DbHelper instance = null;
    private Context context;

    private DbHelper(Context context){
        this.context=context;
    }

    private static DbHelper getInstance(Context context){
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }




}
