package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import model.Beleske;

/**
 * Created by android on 26.11.16..
 */
public class ORMLightHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME    = "priprema.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Beleske, Integer> BeleskeDao = null;


    public ORMLightHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Beleske.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Beleske.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Beleske, Integer> getBeleskeDao() throws SQLException {
        if (BeleskeDao == null) {
            BeleskeDao = getDao(Beleske.class);
        }

        return BeleskeDao;
    }



    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        BeleskeDao = null;


        super.close();
    }
}
