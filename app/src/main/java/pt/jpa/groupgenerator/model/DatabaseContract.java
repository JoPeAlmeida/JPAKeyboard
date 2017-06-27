package pt.jpa.groupgenerator.model;

import android.provider.BaseColumns;

/**
 * Created by sebasi on 26/06/2017.
 */

public final class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private DatabaseContract() {}

    public static abstract class Irmaos implements BaseColumns {

        public static final String TABLE_NAME = "Irmaos";
        public static final String COL_NAME = "NAME";
        public static final String COL_EMAIL = "EMAIL";
        public static final String COL_PHONE = "PHONE";

        public static final String CREATE_TABLE = "CREATE TABLE" +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_PHONE + " INTEGER" + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Score implements BaseColumns {

        public static final String TABLE_NAME = "Score";
        public static final String COL_IRMAO_ID = "IRMAO_ID";
        public static final String COL_PP = "PP";
        public static final String COL_PA = "PA";

        public static final String CREATE_TABLE = "CREATE TABLE" +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_IRMAO_ID + "INTEGER," +
                COL_PP + " INTEGER," +
                COL_PA + " INTEGER," +
                "FOREIGN KEY(" + COL_IRMAO_ID + ") REFERENCES " + Irmaos.TABLE_NAME + "(_ID) )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Historico implements BaseColumns {

        public static final String TABLE_NAME = "Historico";
        public static final String COL_TYPE = "TYPE";
        public static final String COL_DATE = "DATE";
        public static final String COL_NAME = "NAME";
        public static final String COL_IRMAOS = "IRMAOS";

        public static final String CREATE_TABLE = "CREATE TABLE" +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TYPE + " TEXT," +
                COL_DATE + " INTEGER," +
                COL_NAME + " TEXT," +
                COL_IRMAOS + " TEXT" + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
