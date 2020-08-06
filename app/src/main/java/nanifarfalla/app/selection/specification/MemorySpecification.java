package nanifarfalla.app.selection.specification;

/**
 * Patrón de especificación para los productos
 */
public interface MemorySpecification<T> extends Specification{
    boolean isSatisfiedBy(T item);
}
