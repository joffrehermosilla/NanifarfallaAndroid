package nanifarfalla.app.util;

import android.content.Context;

import com.hermosaprogramacion.premium.appproductos.R;

/**
 * Clase de utilidades para solucionar situaciones en las actividades
 */

public class ActivityUtils {
    public static boolean isTwoPane(Context context) {
        return context.getResources().getBoolean(R.bool.twoPane);
    }
}
