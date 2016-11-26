package com.example.android.beleske;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import android.support.v7.widget.Toolbar;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import db.ORMLightHelper;
import model.Beleske;

/**
 * Created by android on 26.11.16..
 */
public class SecondActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Beleske b;

    private EditText naslov;
    private EditText opis;
    private EditText datum_kreiranja;
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(MainActivity.BELESKE_KEY);

        try {
            b = getDatabaseHelper().getBeleskeDao().queryForId(key);

            naslov = (EditText) findViewById(R.id.naslov);
            opis = (EditText) findViewById(R.id.opis);
            datum_kreiranja = (EditText) findViewById(R.id.datum_kreiranja);
           
            naslov.setText(b.getNaslov());
            opis.setText(b.getOpis());
            datum_kreiranja.setText(b.getDatum_kreiranja());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.lista_beleski);

        try {
            List<Beleske> list = getDatabaseHelper().getBeleskeDao().queryBuilder()
                    .where()
                    .eq(Beleske.FIELD_NAME_ID, b.getId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Beleske b = (Beleske) listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, b.getNaslov() + " " + b.getOpis() + " " + b.getDatum_kreiranja(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lista_beleski);

        if (listview != null){
            ArrayAdapter<Beleske> adapter = (ArrayAdapter<Beleske>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Beleske> list = getDatabaseHelper().getBeleskeDao().queryBuilder()
                            .where()
                            .eq(Beleske.FIELD_NAME_ID, b.getId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dodaj_belesku);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){
        //provera podesenja
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.update:
                //POKUPITE INFORMACIJE SA EDIT POLJA
                b.setNaslov(naslov.getText().toString());
                b.setOpis(opis.getText().toString());
                b.setDatum_kreiranja(datum_kreiranja.getText().toString());


                try {
                    getDatabaseHelper().getBeleskeDao().update(b);

                    showMessage("Detalji beleske su osvezeni");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.delete:
                try {
                    getDatabaseHelper().getBeleskeDao().delete(b);

                    showMessage("Beleska obrisana");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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


