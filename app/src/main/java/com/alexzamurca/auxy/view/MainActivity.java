package com.alexzamurca.auxy.view;

import android.os.Bundle;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.model.Crime;
import com.alexzamurca.auxy.model.PoliceAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomNavigationView();

        PoliceAPI papi = new PoliceAPI(getApplicationContext(), "https://data.police.uk/api/crimes-street/all-crime?lat=51.37973&lng=-2.32656&date=2019-01");
        ArrayList<Crime> response = papi.getResponse();
    }

    private void initBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}