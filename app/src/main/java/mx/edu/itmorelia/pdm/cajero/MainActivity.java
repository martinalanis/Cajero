package mx.edu.itmorelia.pdm.cajero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;
    private SharedPreferences pref;
    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
           setupDrawerContent(navigationView);//escuchar item seleccionado
        }

        drawerTitle = getResources().getString(R.string.home_item);
        if (savedInstanceState == null) {
            selectItem(drawerTitle);//crear fragmento dinamicamente dependiendo del item seleccionado
        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Marcar item presionado
                menuItem.setChecked(true);
                //Crear nuevo fragmento
                String title = menuItem.getTitle().toString();
                selectItem(title);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   private void selectItem(String title) {
        //Enviar como argumento del fragmento
        if(title.equalsIgnoreCase("Cerrar Sesión")){
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.IS_LOGGED_IN, false);
            editor.putString(Constants.EMAIL, "");
            editor.putString(Constants.NOMBRE, "");
            editor.putString(Constants.ID, "");
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
            finish();
            startActivity(intent);
        }else{
            Bundle args = new Bundle();
            args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);

            Fragment fragment = PlaceholderFragment.newInstance(title);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
            drawerLayout.closeDrawers(); // Cerrar drawer

            setTitle(title); // Setear título actual
        }

    }

}

