package nanifarfalla.app.addeditinvoice.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Locale;

import nanifarfalla.app.R;
import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;

/**
 * Adaptador de items de factura
 */

public class InvoiceItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemListener {

        void onItemClick(InvoiceItemUi invoiceItem);

        void onRemoveItemClick(InvoiceItemUi invoiceItem);

    }

    private final Context context;

    private List<InvoiceItemUi> invoiceItems;

    private ItemListener listener;


    public class InvoiceItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView productImage;
        public TextView productName;
        public TextView productQuantityXPrice;
        public TextView lineTotal;
        public ImageButton removeButton;

        public InvoiceItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.invoice_item_list_item, parent, false));
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productQuantityXPrice = (TextView) itemView.findViewById(R.id.product_quantity_price);
            lineTotal = (TextView) itemView.findViewById(R.id.line_total);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_button);
            removeButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int i = getAdapterPosition();

            if (i != RecyclerView.NO_POSITION) {
                InvoiceItemUi item = getItem(i);

                if (view.getId() == R.id.remove_button) {
                    listener.onRemoveItemClick(item);
                } else {
                    listener.onItemClick(item);
                }
            }
        }
    }

    public InvoiceItemAdapter(Context context, List<InvoiceItemUi> invoiceItems,
                              ItemListener listener) {
        this.context = context;
        this.invoiceItems = invoiceItems;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvoiceItemViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InvoiceItemViewHolder vh = (InvoiceItemViewHolder) holder;
        InvoiceItemUi item = getItem(position);

        Glide.with(context)
                .load(item.getProductImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(vh.productImage);

        vh.productName.setText(item.getProductName());

        String priceXQuantity = String.format(Locale.ROOT, "$%.2f × %s",
                item.getItemPrice(), item.getQuantity());
        vh.productQuantityXPrice.setText(priceXQuantity);

        String total = String.format(Locale.ROOT, "$%.2f", item.getTotal());
        vh.lineTotal.setText(total);
    }


    @Override
    public int getItemCount() {
        return invoiceItems.size();
    }

    public void replaceData(List<InvoiceItemUi> invoiceItemUis) {
        invoiceItems = invoiceItemUis;
        notifyDataSetChanged();
    }

    private InvoiceItemUi getItem(int position) {
        return invoiceItems.get(position);
    }
}
