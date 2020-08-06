package nanifarfalla.app.selection.selector;

import android.content.ContentResolver;

import java.util.List;

/**
 * Abstracción para selectores de content providers
 */

public interface ProviderSelector<T> extends Selector {
    void selectProviderRows(ContentResolver contentResolver, ProviderSelectorCallback<T> callback);

    interface ProviderSelectorCallback<T> {
        void onDataSelected(List<T> items);
        void onDataNotAvailable(String error);
    }
}
