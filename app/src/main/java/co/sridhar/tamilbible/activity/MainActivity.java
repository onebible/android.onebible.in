package co.sridhar.tamilbible.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.fragments.AboutUsFragment;
import co.sridhar.tamilbible.fragments.FavouriteFragment;
import co.sridhar.tamilbible.fragments.NotesFragment;
import co.sridhar.tamilbible.fragments.SearchFragment;
import co.sridhar.tamilbible.fragments.SectionListingFragment;
import co.sridhar.tamilbible.fragments.SettingsFragment;
import co.sridhar.tamilbible.fragments.ThreadBookmarkFragment;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.task.GetVersionCode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Switch nightModeSwitch;

    public MainActivity() {
        Log.e("onebible_loaded - ", "true");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerView.findViewById(R.id.nav_header_main);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSection();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        boolean isAppLoaded = AppPreferences.getInstance().isAppLoaded(getBaseContext());
        if (!isAppLoaded) {
            AppPreferences.getInstance().markUpdateAsShowed(getBaseContext(), false);
        }

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_night_mode);
        nightModeSwitch = (Switch) menuItem.getActionView().findViewById(R.id.night_mode_switch);
        loadSwitch();
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                AppPreferences.getInstance().saveNightMode(getBaseContext(), isChecked);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        loadSection();
        checkNotifier();

        System.out.println("app_loaded");
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void loadSection() {
        Fragment fragment = new SectionListingFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    private void checkNotifier() {
        boolean isUpdateNotified = AppPreferences.getInstance().isUpdateNotifier(getBaseContext());
        if (!isUpdateNotified) {
            try {
                String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                String packageName = getPackageName();
                new GetVersionCode(getBaseContext(), currentVersion, packageName).execute();
                AppPreferences.getInstance().markUpdateAsShowed(getBaseContext(), true);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_favourite) {
            fragment = new FavouriteFragment();
        } else if (id == R.id.nav_books) {
            fragment = new SectionListingFragment();
        } else if (id == R.id.nav_search) {
            fragment = new SearchFragment();
        } else if (id == R.id.nav_about_us) {
            fragment = new AboutUsFragment();
        } else if (id == R.id.nav_notes) {
            fragment = new NotesFragment();
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareBodyText = "Read verses without ads and share without any marketing tag attached using the Onebible app \r\n https://play.google.com/store/apps/details?id=" + getPackageName();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_thread_bookmark) {
            fragment = new ThreadBookmarkFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_night_mode) {
            return true;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadSwitch() {
        boolean isNightMode = AppPreferences.getInstance().isNightMode(getBaseContext());
        if (isNightMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            nightModeSwitch.setChecked(true);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            nightModeSwitch.setChecked(false);
        }
    }
    
}


