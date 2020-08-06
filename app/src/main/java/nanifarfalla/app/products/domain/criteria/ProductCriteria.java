package nanifarfalla.app.products.domain.criteria;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;

/**
 * Patrón de especificación para los productos
 */
public interface ProductCriteria {
    List<Product> match(List<Product> products);
}
