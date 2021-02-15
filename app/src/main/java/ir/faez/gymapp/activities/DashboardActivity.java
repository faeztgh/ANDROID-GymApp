package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.UserCudAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.databinding.ActivityDashboardBinding;
import ir.faez.gymapp.utils.Action;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ActivityDashboardBinding binding;

    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        // initializing AppData
        appData = (AppData) getApplication();

        // initializing binding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // handling drawer
        drawerhandler();

        // handle dashboard user indo
        userInfoHandler();

        // invoke Listeners
        invokeOnClickListeners();
    }

    private void invokeOnClickListeners() {
        binding.userAvatarIv.setOnClickListener(this);
        binding.allCoursesIv.setOnClickListener(this);
        binding.myCoursesIv.setOnClickListener(this);
        binding.journalsIv.setOnClickListener(this);
        binding.profileIv.setOnClickListener(this);
        binding.walletIv.setOnClickListener(this);
        binding.shareIv.setOnClickListener(this);
    }

    private void userInfoHandler() {
        if (appData.getCurrentUser() != null) {
            binding.dashboardUserName.setText(" " + appData.getCurrentUser().getFullName());
        }
    }


    private void drawerhandler() {

        drawerLayout = (DrawerLayout) findViewById(R.id.dashboard_activity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.drawer_dashboard:
                        Toast.makeText(DashboardActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile_iv:
                        profileHandler();
                        break;
                    case R.id.drawer_about:
                        Toast.makeText(DashboardActivity.this, "My Cart", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_logout:
                        logoutHandler();
                        break;
                    case R.id.drawer_exit:
                        exitFromAppHandler();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

    }



    private void logoutHandler() {
        UserCudAsyncTask userCudAsyncTask = new UserCudAsyncTask(this, Action.UPDATE_ACTION,
                new DbResponse<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            appData.setCurrentUser(null);
//                            moveTaskToBack(true);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(DashboardActivity.this,
                                R.string.cantLogout, Toast.LENGTH_SHORT).show();
                    }
                });

        User newUser = appData.getCurrentUser();
        newUser.setIsLoggedIn("false");
        appData.setCurrentUser(newUser);
        userCudAsyncTask.execute(appData.getCurrentUser());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_courses_iv:
                allCourseHandler();

                break;
            case R.id.my_courses_iv:
                Toast.makeText(this, R.string.optionNotImplYet, Toast.LENGTH_SHORT).show();

                break;
            case R.id.profile_iv:
                profileHandler();

                break;
            case R.id.user_avatar_iv:
                profileHandler();

                break;
            case R.id.share_iv:
                Toast.makeText(this, R.string.optionNotImplYet, Toast.LENGTH_SHORT).show();

                break;
            case R.id.wallet_iv:
                Toast.makeText(this, R.string.optionNotImplYet, Toast.LENGTH_SHORT).show();

                break;
            case R.id.journals_iv:
                Toast.makeText(this, R.string.optionNotImplYet, Toast.LENGTH_SHORT).show();

                break;

            default:
                Toast.makeText(this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
        }
    }

    //************************************* Navigation Handlers ******************************************
    private void allCourseHandler() {
        Intent intent = new Intent(getApplicationContext(), AllCoursesActivity.class);
        startActivity(intent);
    }

    private void profileHandler() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    private void exitFromAppHandler() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}