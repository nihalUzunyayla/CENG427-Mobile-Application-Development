package com.example.pawrescue;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.annotation.NonNull;
import com.example.pawrescue.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_main_menu, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return MainActivity.this.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch events with GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Detect left and right swipes
        float deltaX = e2.getX() - e1.getX();
        if (Math.abs(deltaX) > 100 && Math.abs(velocityX) > 100) {
            if (deltaX > 0) {
                // Swipe right
                navigateToNextFragment();
                return true;
            } else {
                // Swipe left
                navigateToPreviousFragment();
                return true;
            }
        }
        return false;
    }

    private void navigateToNextFragment() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        int currentFragmentId = navController.getCurrentDestination().getId();

        if (currentFragmentId == R.id.navigation_settings) {
            navController.navigate(R.id.navigation_main_menu);
        } else if (currentFragmentId == R.id.navigation_main_menu) {
            navController.navigate(R.id.navigation_home);
        }

    }

    private void navigateToPreviousFragment() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        int currentFragmentId = navController.getCurrentDestination().getId();

        if (currentFragmentId == R.id.navigation_main_menu) {
            navController.navigate(R.id.navigation_settings);
        } else if (currentFragmentId == R.id.navigation_home) {
                navController.navigate(R.id.navigation_main_menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        if (item.getItemId() == R.id.navigation_home) {
            navController.navigate(R.id.navigation_home);
            return true;
        } else if (item.getItemId() == R.id.navigation_main_menu) {
            navController.navigate(R.id.navigation_main_menu);
            return true;
        } else if (item.getItemId() == R.id.navigation_settings) {
            navController.navigate(R.id.navigation_settings);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}