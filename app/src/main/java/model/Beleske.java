package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by android on 26.11.16..
 */
@DatabaseTable(tableName = Beleske.TABLE_NAME_USERS)
public class Beleske {

        public static final String TABLE_NAME_USERS = "Beleska";
        public static final String FIELD_NAME_ID     = "id";
        public static final String TABLE_MOVIE_NASLOV = "naslov";
        public static final String TABLE_MOVIE_OPIS = "opis";
        public static final String TABLE_MOVIE_DATUM_KREIRANJA = "datum_kreiranja";

        @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
        private int Id;

        @DatabaseField(columnName = TABLE_MOVIE_NASLOV)
        private String Naslov;

        @DatabaseField(columnName = TABLE_MOVIE_OPIS)
        private String Opis;

        @DatabaseField(columnName = TABLE_MOVIE_DATUM_KREIRANJA)
        private String Datum_kreiranja;


        public Beleske() {
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            this.Id = id;
        }

        public String getNaslov() {
            return Naslov;
        }

        public void setNaslov(String naslov) {
            this.Naslov = naslov;
        }

        public String getOpis() {
            return Opis;
        }

        public void setOpis(String opis) {
            this.Opis = opis;
        }


        public String getDatum_kreiranja() {
            return Datum_kreiranja;
        }

        public void setDatum_kreiranja(String datum_kreiranja) {
            this.Datum_kreiranja = datum_kreiranja;
        }

        @Override
        public String toString() {
            return Naslov;
        }
    }
