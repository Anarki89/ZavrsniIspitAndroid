package com.example.android.beleske;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;
import java.util.prefs.Preferences;

import db.ORMLightHelper;
import model.Beleske;

public class MainActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;

    public static String BELESKE_KEY = "BELESKE_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.lista_beleski);

        try {
            List<Beleske> list = getDatabaseHelper().getBeleskeDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Beleske b = (Beleske) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(BELESKE_KEY, b.getId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lista_beleski);

        if (listview != null){
            ArrayAdapter<Beleske> adapter = (ArrayAdapter<Beleske>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Beleske> list = getDatabaseHelper().getBeleskeDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.dodaj_belesku:
                //DIALOG ZA UNOS PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dodaj_belesku1);

                Button add = (Button) dialog.findViewById(R.id.dodaj_belesku2);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText naslov = (EditText) dialog.findViewById(R.id.naslov);
                        EditText opis = (EditText) dialog.findViewById(R.id.opis);
                        EditText datum_kreiranja = (EditText) dialog.findViewById(R.id.datum_kreiranja);

                        Beleske b = new Beleske();
                        b.setNaslov(naslov.getText().toString());
                        b.setOpis(opis.getText().toString());
                        b.setDatum_kreiranja(datum_kreiranja.getText().toString());

                        try {
                            getDatabaseHelper().getBeleskeDao().create(b);

                            //provera podesenja
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast){
                                Toast.makeText(MainActivity.this, "Added new actor", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Added new actor");
                            }

                            //REFRESH
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();

                break;
            case R.id.about:

                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, Preferences.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dodaj_belesku);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    //Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
