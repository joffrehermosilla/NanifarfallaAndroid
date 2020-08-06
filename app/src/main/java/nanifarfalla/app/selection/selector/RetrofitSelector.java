package nanifarfalla.app.selection.selector;

import java.util.List;

import retrofit2.Retrofit;

/**
 * Abstracción para selectores con uso de Retrofit
 */

public interface RetrofitSelector<T> extends Selector {
    void selectRetrofitRows(Retrofit retrofit, RetrofitSelectorCallback<T> callback);

    interface RetrofitSelectorCallback<T> {
        void onDataSelected(List<T> items);
        void onDataNotAvailable(String error);
    }
}
