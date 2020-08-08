package nanifarfalla.app.products.presentation;

/**
 * Permite saber si un elemento relacionado con datos está cargandolos o si no posee más
 */
public interface DataLoading {
    boolean isLoadingData();

    boolean isThereMoreData();
}
