package com.abhishek360.dev.logicsynthesis;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
                                                                    AndGateFragment.OnFragmentInteractionListener,
                                                                        OrGateFragment.OnFragmentInteractionListener,
                                                XorGateFragment.OnFragmentInteractionListener,AdderFragment.OnFragmentInteractionListener,

        NavigationView.OnNavigationItemSelectedListener
{


    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Fragment fragment;
    private FragmentTransaction ft;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment=new HomeFragment();
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_frame,fragment);
        ft.commit();


        navigationView= findViewById(R.id.navigation_view);
        drawerLayout= findViewById(R.id.drawer_layout);

        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        navigationView.setCheckedItem(R.id.home_menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {

            return true;
         }



        return super.onOptionsItemSelected(item);
    }

    public static void tosty(Context ctx,String msg)
    {
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id= menuItem.getItemId();
        Bundle bundle= new Bundle();
        int tabCode;

        switch (id) {
            case R.id.home_menu:
                fragment = new HomeFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.and_gate_menu:

                fragment = new AndGateFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame, fragment);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft.addToBackStack(null);

                ft.commit();
                break;

            case R.id.or_gate_menu:
                fragment = new OrGateFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;


            case R.id.XOR_gate_menu:
                fragment = new XorGateFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;


            case R.id.half_adder_menu:
                fragment = new AdderFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;
        }
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();


        return false;

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager= getSupportFragmentManager();
        navigationView.setCheckedItem(R.id.home_menu);


        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStackImmediate();
        else super.onBackPressed();



    }

}
