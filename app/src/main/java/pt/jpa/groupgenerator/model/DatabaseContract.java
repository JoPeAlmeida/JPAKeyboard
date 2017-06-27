package pt.jpa.groupgenerator.model;

import android.provider.BaseColumns;

/**
 * Created by sebasi on 26/06/2017.
 */

public final class DatabaseContract {

    static final  int    DATABASE_VERSION   = 1;
    static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private DatabaseContract() {}

    public static abstract class Irmaos implements BaseColumns {

        static final String TABLE_NAME = "Irmaos";
        public static final String COL_NAME = "NAME";
        public static final String COL_EMAIL = "EMAIL";
        public static final String COL_PHONE = "PHONE";

        static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_PHONE + " INTEGER" + " )";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class Score implements BaseColumns {

        static final String TABLE_NAME = "Score";
        static final String COL_IRMAO_ID = "IRMAO_ID";
        static final String COL_PP = "PP";
        static final String COL_PA = "PA";

        static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_IRMAO_ID + " INTEGER," +
                COL_PP + " INTEGER," +
                COL_PA + " INTEGER," +
                "FOREIGN KEY(" + COL_IRMAO_ID + ") REFERENCES " + Irmaos.TABLE_NAME + "(_ID) )";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class Historico implements BaseColumns {

        static final String TABLE_NAME = "Historico";
        static final String COL_TYPE = "TYPE";
        static final String COL_DATE = "DATE";
        static final String COL_NAME = "NAME";
        static final String COL_IRMAOS = "IRMAOS";

        static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TYPE + " TEXT," +
                COL_DATE + " INTEGER," +
                COL_NAME + " TEXT," +
                COL_IRMAOS + " TEXT" + " )";
        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
