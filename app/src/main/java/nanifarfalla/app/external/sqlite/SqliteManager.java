package nanifarfalla.app.external.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hermosaprogramacion.premium.appproductos.external.sqlite.DatabaseContract.Products;

/**
 * Manejador de creación y versionamiento de la base de datos
 */

public class SqliteManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_productos.db";

    private static final int CUR_DATABASE_VERSION = 1;

    public SqliteManager(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Products.TABLE_NAME + " ("
                + Products._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Products.CODE + " TEXT NOT NULL,"
                + Products.NAME + " TEXT NOT NULL,"
                + Products.DESCRIPTION + " TEXT NOT NULL,"
                + Products.BRAND + " TEXT NOT NULL,"
                + Products.PRICE + " REAL NOT NULL,"
                + Products.UNITS_IN_STOCK + " INTEGER NOT NULL,"
                + Products.IMAGE_URL + " TEXT NOT NULL DEFAULT '',"
                + "UNIQUE (" + Products.CODE + ") )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No se requiere en esta versión
    }
}
