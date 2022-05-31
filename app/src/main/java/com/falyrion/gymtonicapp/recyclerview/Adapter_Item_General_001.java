package com.falyrion.gymtonicapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falyrion.gymtonicapp.R;

import java.util.List;

public class Adapter_Item_General_001 extends RecyclerView.Adapter<Adapter_Item_General_001.Viewholder> {

    /**
     * This class is the recyclerview-adapter for the item item_005.
     * This item contains a textview and a delete-button.
     */

    public class Viewholder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutMain;
        TextView textViewTitle;
        ImageButton buttonRemove;
        Interface_Item_Edit interfaceItemEdit;

        public Viewholder(@NonNull View itemView, Interface_Item_Edit interfaceItemEdit) {
            super(itemView);
            linearLayoutMain = itemView.findViewById(R.id.layoutMain);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonRemove = itemView.findViewById(R.id.buttonDeleteItem);
            this.interfaceItemEdit = interfaceItemEdit;
        }
    }

    // Interface -----------------------------------------------------------------------------------

    public interface Interface_Item_Edit {
        void onItemClicked(int itemPosition);
        void onButtonRemoveClicked(int itemPosition);
    }

    // Constructor of this class -------------------------------------------------------------------

    private List<Item_General_001> itemsList;
    private Interface_Item_Edit interfaceItemEdit;

    public Adapter_Item_General_001(List<Item_General_001> itemsList, Interface_Item_Edit interfaceItemEdit) {
        this.itemsList = itemsList;
        this.interfaceItemEdit = interfaceItemEdit;
    }

    // Functions for this class --------------------------------------------------------------------

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate custom layout
        View view = inflater.inflate(R.layout.recyclerview_item_general_001, parent, false);

        // Return new holder instance
        return new Viewholder(view, interfaceItemEdit);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        Item_General_001 item = itemsList.get(position);

        // Set item views based on views and data-class
        String itemTitle = item.getTitle();
        holder.textViewTitle.setText(itemTitle);  // Title
        holder.linearLayoutMain.setOnClickListener(view -> {
            holder.interfaceItemEdit.onItemClicked(holder.getAdapterPosition());
        });

        // Set onClick-Events
        holder.buttonRemove.setOnClickListener(view -> {
            holder.interfaceItemEdit.onButtonRemoveClicked(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
