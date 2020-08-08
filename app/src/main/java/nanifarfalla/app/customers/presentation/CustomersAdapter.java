package nanifarfalla.app.customers.presentation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Preconditions;

import java.util.List;

import nanifarfalla.app.R;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.products.presentation.DataLoading;

/**
 * Adaptador para la lista de clientes
 */

public class CustomersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DataLoading {

    private final static int TYPE_ITEM = 1;
    private final static int TYPE_NEXT_PAGE_INDICATOR = 2;

    private List<Customer> mItems;

    private boolean mIsLoading = false;
    private boolean mEndless = false;



    public interface CustomerItemListener {
        void onCustomerClick(Customer clickedCustomer);

    }

    private CustomerItemListener mItemListener;


    public CustomersAdapter(List<Customer> customers, CustomerItemListener itemListener) {
        setList(customers);
        mItemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view; // View a retornar

        if (viewType == TYPE_NEXT_PAGE_INDICATOR) {
            view = inflater.inflate(R.layout.item_loading_footer, parent, false);
            return new NextPageIndicatorHolder(view);
        }

        view = inflater.inflate(R.layout.customer_item, parent, false);
        return new CustomersHolder(view, mItemListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                bindCustomersHolder((CustomersHolder) holder, position);
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

    private Customer getItem(int position) {
        return mItems.get(position);
    }

    private void setList(List<Customer> customers) {
        mItems = Preconditions.checkNotNull(customers);
    }

    private void bindCustomersHolder(CustomersHolder holder, int position) {
        Customer customer = mItems.get(position);
        holder.name.setText(customer.getName());
        holder.phone.setText(customer.getPhone());
    }

    private void bindNextPageIndicatorHolder(NextPageIndicatorHolder holder, int position) {
        boolean showNextPageIndicator = position > 0 && mIsLoading && mEndless;
        holder.progress.setVisibility(showNextPageIndicator ? View.VISIBLE : View.INVISIBLE);
    }

    public void replaceItems(List<Customer> customers) {
        setList(customers);
        notifyDataSetChanged();
    }

    public void addItems(List<Customer> customers) {
        mItems.addAll(customers);
    }

    private int getNPIPosition() {
        return mIsLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    public void dataStartedLoading() {
        if (mIsLoading) {
            return;
        }

        mIsLoading = true; // Carga de datos On

        // Notificar inserción del indicador de nueva página
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getNPIPosition()); // NPI (Next Page Indicator)
            }
        });

    }

    public void dataFinishedLoading() {
        if (!mIsLoading) {
            return;
        }

        mIsLoading = false; // Carga de datos Off

        // Notificar eliminación del indicador de nueva página
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(getNPIPosition());
            }
        });
    }


    public void setEndless(boolean endless) {
        mEndless = endless;
    }

    @Override
    public boolean isLoadingData() {
        return mIsLoading;
    }

    @Override
    public boolean isThereMoreData() {
        return mEndless;
    }

    private class CustomersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView phone;

        private CustomerItemListener mItemListener;

        public CustomersHolder(View itemView, CustomerItemListener itemListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.customer_name);
            phone = (TextView) itemView.findViewById(R.id.customer_phone);
            mItemListener = itemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Customer customer = getItem(position);
            mItemListener.onCustomerClick(customer);
        }
    }

    private class NextPageIndicatorHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress;

        public NextPageIndicatorHolder(View view) {
            super(view);
            progress = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

}
