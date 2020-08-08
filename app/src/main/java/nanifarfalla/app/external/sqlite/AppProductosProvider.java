package nanifarfalla.app.external.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

import nanifarfalla.app.external.sqlite.DatabaseContract.Products;

public class AppProductosProvider extends ContentProvider {


    private SqliteManager mSqliteManager;

    private static final int PRODUCTS = 100;
    private static final int PRODUCTS_CODE = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "products", PRODUCTS);
        matcher.addURI(authority, "products/*", PRODUCTS_CODE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mSqliteManager = new SqliteManager(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS: {
                return DatabaseContract.makeContentType("products");
            }
            case PRODUCTS_CODE: {
                return DatabaseContract.makeContentItemType("products");
            }
        }
        throw new UnsupportedOperationException("Uri no soportada: " + uri);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PRODUCTS:
                retCursor = mSqliteManager.getReadableDatabase().query(
                        Products.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCTS_CODE:
                String[] where = {Products.getCode(uri)};
                retCursor = mSqliteManager.getReadableDatabase().query(
                        Products.TABLE_NAME,
                        projection,
                        Products.CODE + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Uri no soportada: " + uri);
        }
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mSqliteManager.getWritableDatabase();
        Uri returnUri;
        String productCode;

        switch (sUriMatcher.match(uri)) {
            case PRODUCTS: {
                productCode = values.getAsString(Products.CODE);
                Cursor exists = db.query(
                        Products.TABLE_NAME,
                        new String[]{Products.CODE},
                        Products.CODE + " = ?",
                        new String[]{productCode},
                        null,
                        null,
                        null
                );
                if (exists.moveToLast()) {
                    long _id = db.update(
                            Products.TABLE_NAME, values,
                            Products.CODE + " = ?",
                            new String[]{productCode}
                    );
                    if (_id > 0) {
                        returnUri = Products.buildUriWith(productCode);
                    } else {
                        throw new SQLException("No se pudo insertar en " + uri);
                    }
                } else {
                    db.insertOrThrow(Products.TABLE_NAME, null, values);
                    returnUri = Products.buildUriWith(productCode);
                }
                exists.close();
                break;
            }

            default:
                throw new UnsupportedOperationException("Uri no soportada:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mSqliteManager.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PRODUCTS: {
                rowsUpdated = db.update(Products.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case PRODUCTS_CODE: {
                String[] args = {Products.getCode(uri)};
                rowsUpdated = db.update(
                        Products.TABLE_NAME,
                        values,
                        Products.CODE + " = ?",
                        args
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Uri no soportada: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mSqliteManager.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case PRODUCTS: {
                rowsDeleted = db.delete(
                        Products.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PRODUCTS_CODE: {
                String[] args = {Products.getCode(uri)};
                rowsDeleted = db.delete(
                        Products.TABLE_NAME,
                        Products.CODE + " = ?",
                        args
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri no soportada: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
}
