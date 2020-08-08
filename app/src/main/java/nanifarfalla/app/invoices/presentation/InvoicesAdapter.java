package nanifarfalla.app.invoices.presentation;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Locale;

import nanifarfalla.app.R;
import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.products.presentation.DataLoading;
import nanifarfalla.app.util.DateTimeUtils;

/**
 * Adaptador para la lista de facturas
 */

public class InvoicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DataLoading {

    private final static int TYPE_ITEM = 1;
    private final static int TYPE_NEXT_PAGE_INDICATOR = 2;

    private final Resources mResources;

    private List<InvoiceUi> mItems;

    private boolean mIsLoading = false;
    private boolean mEndless = false;


    public InvoicesAdapter(Context context, List<InvoiceUi> invoicesInfos) {
        mResources = context.getResources();
        setList(invoicesInfos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view; // View a retornar

        if (viewType == TYPE_NEXT_PAGE_INDICATOR) {
            view = inflater.inflate(R.layout.item_loading_footer, parent, false);
            return new NextPageIndicatorHolder(view);
        }

        view = inflater.inflate(R.layout.invoice_item, parent, false);
        return new InvoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                bindInvoiceHolder((InvoiceHolder) holder, position);
                break;
            case TYPE_NEXT_PAGE_INDICATOR:
                bindNextPageIndicatorHolder((NextPageIndicatorHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemsSize = mItems.size();

        // Lógica para determinar cuándo usar los tipos de views
        if (position < itemsSize && itemsSize > 0) {
            return TYPE_ITEM;
        }
        return TYPE_NEXT_PAGE_INDICATOR;
    }

    @Override
    public int getItemCount() {
        return mItems.size() + (mIsLoading ? 1 : 0);
    }

    private void setList(List<InvoiceUi> invoices) {
        mItems = Preconditions.checkNotNull(invoices);
    }

    private void bindInvoiceHolder(InvoiceHolder holder, int position) {
        InvoiceUi invoice = mItems.get(position);

        String totalItems = mResources.getQuantityString(R.plurals.item_plurals,
                invoice.getTotalItems(), invoice.getTotalItems());
        String totalAmount = String.format(Locale.ROOT, "$%.2f", invoice.getTotalAmount());
        String date = DateTimeUtils.formatDate(invoice.getDate(), DateTimeUtils.DATE_ONLY_PATTERN);

        holder.customerName.setText(invoice.getCustomerName());
        holder.number.setText(invoice.getNumber());
        holder.date.setText(date);
        holder.totalAmount.setText(totalAmount);

        int backgroundColor = mResources.getColor(R.color.white);
        switch (invoice.getState()) {
            case Invoice.STATE_VALUE_DRAFT:
                backgroundColor = mResources.getColor(R.color.state_draft);
                break;
            case Invoice.STATE_VALUE_PAID:
                backgroundColor = mResources.getColor(R.color.state_paid);
                break;
            case Invoice.STATE_VALUE_CANCELED:
                backgroundColor = mResources.getColor(R.color.state_canceled);
                break;
            case Invoice.STATE_VALUE_SENT:
                backgroundColor = mResources.getColor(R.color.state_sent);
                break;
        }
        holder.state.setText(invoice.getState());
        holder.state.setTextColor(backgroundColor);
        holder.totalItems.setText(totalItems);
    }

    private void bindNextPageIndicatorHolder(NextPageIndicatorHolder holder, int position) {
        boolean showNextPageIndicator = position > 0 && mIsLoading && mEndless;
        holder.progress.setVisibility(showNextPageIndicator ? View.VISIBLE : View.INVISIBLE);
    }

    public void dataStartedLoading() {
        if (mIsLoading) {
            return;
        }

        mIsLoading = true;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getNextPageIndicatorPosition());
            }
        });

    }

    public void dataFinishedLoading() {
        if (!mIsLoading) {
            return;
        }

        mIsLoading = false;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(getNextPageIndicatorPosition());
            }
        });
    }

    public void setEndless(boolean endless) {
        mEndless = endless;
    }

    public void replaceItems(List<InvoiceUi> invoices) {
        setList(invoices);
        notifyDataSetChanged();
    }

    public void addItems(List<InvoiceUi> invoices) {
        mItems.addAll(invoices);
    }

    private int getNextPageIndicatorPosition() {
        return mIsLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public boolean isLoadingData() {
        return mIsLoading;
    }

    @Override
    public boolean isThereMoreData() {
        return mEndless;
    }

    private class InvoiceHolder extends RecyclerView.ViewHolder {

        TextView customerName;
        TextView number;
        TextView date;
        TextView totalAmount;
        TextView state;
        TextView totalItems;

        InvoiceHolder(View itemView) {
            super(itemView);
            customerName = (TextView) itemView.findViewById(R.id.invoice_customer_name);
            number = (TextView) itemView.findViewById(R.id.invoice_number);
            date = (TextView) itemView.findViewById(R.id.invoice_date);
            totalAmount = (TextView) itemView.findViewById(R.id.invoice_total_amount);
            state = (TextView) itemView.findViewById(R.id.invoice_state);
            totalItems = (TextView) itemView.findViewById(R.id.invoice_total_items);
        }

    }

    private class NextPageIndicatorHolder extends RecyclerView.ViewHolder {
        ProgressBar progress;

        NextPageIndicatorHolder(View view) {
            super(view);
            progress = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}
