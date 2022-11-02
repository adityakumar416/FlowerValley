package com.example.flowersvalley.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowersvalley.ItemClickListener;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.model.AddressModel;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<AddressModel> list;
    Context context;
    ItemClickListener itemClickListener;
    int selectedPosition = -1;


    public AddressAdapter(ArrayList<AddressModel> list, Context context, ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setItems(ArrayList<AddressModel> cartModels) {
        this.list = cartModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressModel addressModel = list.get(position);
        sharedPreferenceManager = new SharedPreferenceManager(context);

        holder.Name.setText("" + addressModel.getName() + ", ");
        holder.pinCode.setText("" + addressModel.getPostalCode());
        holder.completeAddress.setText("" + addressModel.getHouseNo() + ", " + addressModel.getAddress()
                + ", " + addressModel.getCity() + ", " + addressModel.getDistrict() + ", " + addressModel.getState());

        // Checked selected radio button
        holder.selectAddress.setChecked(position == selectedPosition);
        // set listener on radio button
        holder.selectAddress.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        // check condition
                        if (b) {
                            // When checked
                            // update selected position
                            selectedPosition = holder.getAdapterPosition();
                            // Call listener
                            itemClickListener.onClick("" + addressModel.getAddressId());
                        }
                    }
                });

    }

    @Override
    public long getItemId(int position) {
        // pass position
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Name, pinCode, completeAddress;
        RadioButton selectAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            pinCode = itemView.findViewById(R.id.pin_code);
            completeAddress = itemView.findViewById(R.id.complete_address);
            selectAddress = itemView.findViewById(R.id.select);
        }
    }
}
