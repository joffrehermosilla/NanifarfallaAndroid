package nanifarfalla.app.selection.selector;

import java.util.List;

/**
 * Abstracción para selectores de listas Java
 */

public interface ListSelector<T> extends Selector {
    List<T> selectListRows(List<T> items);
}
