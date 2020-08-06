package nanifarfalla.app.external.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contrato de la base de datos SQLite local
 */

public class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.hermosaprogramacion.premium.appproductos";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_PRODUCTS = "products";

    // Tipos MIME
    public static final String CONTENT_TYPE_APP_BASE = "appproductos.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd."
            + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd."
            + CONTENT_TYPE_APP_BASE;

    public static String makeContentType(String resource) {
        if (resource != null) {
            return CONTENT_TYPE_BASE + resource;
        } else {
            return null;
        }
    }

    public static String makeContentItemType(String resource) {
        if (resource != null) {
            return CONTENT_ITEM_TYPE_BASE + resource;
        } else {
            return null;
        }
    }


    public static abstract class Products implements BaseColumns {
        public static final String TABLE_NAME = "product";

        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String BRAND = "brand";
        public static final String PRICE = "phone";
        public static final String UNITS_IN_STOCK = "unitsInStock";
        public static final String IMAGE_URL = "imageUrl";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PRODUCTS).build();

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUriWith(String code) {
            return CONTENT_URI.buildUpon().appendPath(code).build();
        }

        public static String getCode(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
