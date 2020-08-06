package nanifarfalla.app.selection;

import android.support.annotation.Nullable;

import com.hermosaprogramacion.premium.appproductos.selection.specification.Specification;

/**
 * Representa una consulta sobre una fuente de datos
 */

public class Query {
    public static final int ASC_ORDER = 1;
    public static final int DESC_ORDER = 2;

    @Nullable
    private Specification mSpecification;
    @Nullable
    private String mFieldSort;
    private int mSortOrder;
    private int mPageNumber;
    private int mPageSize;

    public Query(@Nullable Specification specification, @Nullable String fieldSort,
                 int sortOrder, int pageNumber, int pageSize) {
        mSpecification = specification;
        mFieldSort = fieldSort;
        mSortOrder = sortOrder;
        mPageNumber = pageNumber;
        mPageSize = pageSize;
    }

    public Query(@Nullable Specification specification) {
        this(specification, null, 0, 0, 0);
    }

    public Specification getSpecification() {
        return mSpecification;
    }

    public void setSpecification(Specification mSpecification) {
        this.mSpecification = mSpecification;
    }

    public String getFieldSort() {
        return mFieldSort;
    }

    public void setFieldSort(String mFieldSort) {
        this.mFieldSort = mFieldSort;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(int mSortOrder) {
        this.mSortOrder = mSortOrder;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public void setPageNumber(int mPageNumber) {
        this.mPageNumber = mPageNumber;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
    }

}
