package nanifarfalla.app.addeditinvoice.domain.entities;

/**
 * Producto para UI de lista de items de factura
 */

public class ProductUi{
    private String mName;
    private int mStock;
    private String mImageUrl;

    public ProductUi(String name,
                     int stock,
                     String imageUrl) {
        mName = name;
        mStock = stock;
        mImageUrl = imageUrl;
    }

    private void setName(String name) {
        this.mName = name;
    }

    private void setStock(int stock) {
        mStock = stock;
    }

    private void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public int getStock() {
        return mStock;
    }

    public String getImageUrl() {
        return mImageUrl;
    }


}
