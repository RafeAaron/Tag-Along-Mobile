package com.example.tagalong.payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    String[] paymentDetails = {"Day one", "Day Two", "Day Three"};
    public static class PaymentViewHolder extends RecyclerView.ViewHolder{

        public TextView txtView;
        public PaymentViewHolder(View view)
        {
            super(view);
            txtView = (TextView) view.findViewById(R.id.transferReason);
        }

        public TextView getTextView() {
            return txtView;
        }
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_view, parent, false);

        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {

        //Attach details to views
        holder.getTextView().setText(paymentDetails[position]);

    }

    @Override
    public int getItemCount() {
        return paymentDetails.length;
    }
}
