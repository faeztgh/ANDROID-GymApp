package ir.faez.gymapp.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.GetSpecificUserAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.databinding.ActivityIntroBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.Status;

import static ir.faez.gymapp.data.AppData.CHANNEL_1_ID;

public class PreLoaderActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    private AppData appData;
    private NotificationManagerCompat notificationManager;
    private NetworkHelper networkHelper;
    private List<CourseReservation> allCourseReservations;
    private List<CourseReservation> pendingCourseReservations;
    private List<CourseReservation> newReservedCourseReservations;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        // initializing AppData
        appData = (AppData) getApplication();

        // init networkHelper
        networkHelper = NetworkHelper.getInstance(this);

        // initializing binding
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

        // check if user loggedin
        getUserByState();

        //init notif manager
        notificationManager = NotificationManagerCompat.from(this);

        // init newReservedCourseReservations
        newReservedCourseReservations = new ArrayList<>();


    }

    private void compareLists() {

        for (CourseReservation pCr : pendingCourseReservations) {
            for (CourseReservation aCr : allCourseReservations) {
                if (pCr.getCourseId().equals(aCr.getCourseId())
                        && !pCr.getStatus().equals(aCr.getStatus())
                        && aCr.getReservationCode() != null) {
                    newReservedCourseReservations.add(aCr);
                }
            }
        }
    }

    private void loadAllCourseReservationsFromServer(String state) {
        networkHelper.getSpecificCourseReservationByOwnerId(appData.getCurrentUser(), result -> {
            if (state.equals(Status.PENDING)) {

                pendingCourseReservations = result != null ? result.getItems() : null;

                // removing old reserved one from list
                assert pendingCourseReservations != null;
                if (pendingCourseReservations.size() != 0) {
                    List<CourseReservation> removed = new ArrayList<>();
                    for (CourseReservation cr : pendingCourseReservations) {
                        if (cr.getStatus().equals(Status.RESERVED)) {
                            removed.add(cr);
                        }
                    }
                    pendingCourseReservations.removeAll(removed);
                }

            }

            allCourseReservations = result != null ? result.getItems() : null;


            compareLists();

            if (newReservedCourseReservations.size() != 0) {
                for (CourseReservation cr : newReservedCourseReservations) {
                    sendOnChannel1(cr);
                    newReservedCourseReservations.remove(cr);
                    loadAllCourseReservationsFromServer(Status.PENDING);
                }
            }


        });
    }


    public void sendOnChannel1(CourseReservation courseReservation) {
        String title = "One of your courses are confirmed!";
        String message = "Reservation Code: " + courseReservation.getReservationCode();
        PendingIntent pendingIntent =  PendingIntent.getActivity(this,1,
                new Intent(this,MyCourseActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.baseline_fitness_center_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_gymapp_foreground))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setColorized(true)
                .setTicker(title)
                .setColor(Color.parseColor("#E51C64"))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(1, notification);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboardActivity() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
    }


    private void getUserByState() {

        GetSpecificUserAsyncTask getSpecificUserAsyncTask = new GetSpecificUserAsyncTask(this,
                Action.GET_BY_STATE_ACTION, new DbResponse<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.getIsLoggedIn().equals("true")) {
                    appData.setCurrentUser(user);

                    // init pending course reservations
                    loadAllCourseReservationsFromServer(Status.PENDING);

                    //init notif
                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Log.i("FAEZ_TEST", "A Kiss every 5 seconds");
                            loadAllCourseReservationsFromServer("ALL");
                        }
                    }, 0, 60000);


                    navigateToDashboardActivity();
                } else {
                    appData.setCurrentUser(null);
                }
            }

            @Override
            public void onError(Error error) {
                navigateToLoginActivity();
            }
        });
        getSpecificUserAsyncTask.execute("true");

    }

}
