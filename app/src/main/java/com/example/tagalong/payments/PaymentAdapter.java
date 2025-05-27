package com.example.tagalong.payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    String[] paymentDetails;
    String[] recieverID;

    String[] dates;
    String[] amounts;
    public static class PaymentViewHolder extends RecyclerView.ViewHolder{

        public TextView transferView;
        public TextView recieverIDView;
        public TextView dateView;
        public TextView amountView;
        public PaymentViewHolder(View view)
        {
            super(view);
            transferView = (TextView) view.findViewById(R.id.transferReason);
            recieverIDView = (TextView) view.findViewById(R.id.recieverDetailss);
            dateView = (TextView) view.findViewById(R.id.dateOfTransfer);
            amountView = (TextView) view.findViewById(R.id.transferAmount);
        }

        public TextView getTextView() {
            return transferView;
        }
    }

    public PaymentAdapter(JSONObject jsonObject)
    {

        try {
            JSONArray arr = jsonObject.getJSONArray("Payments");

            this.paymentDetails = new String[arr.length()];
            this.recieverID = new String[arr.length()];
            this.amounts = new String[arr.length()];
            this.dates = new String[arr.length()];

            for(int a = 0; a < arr.length(); a++)
            {
                paymentDetails[a] ="Reason: " + arr.getJSONObject(a).get("reason").toString();
                recieverID[a] = "Reciever id: " + arr.getJSONObject(a).get("recieverID").toString();
                amounts[a] = "UGX: " + arr.getJSONObject(a).get("amount").toString();
                dates[a] = arr.getJSONObject(a).get("dateCompleted").toString();
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
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
        holder.amountView.setText(amounts[position]);
        holder.dateView.setText(dates[position]);
        holder.recieverIDView.setText(recieverID[position]);
        holder.transferView.setText(paymentDetails[position]);

    }

    @Override
    public int getItemCount() {
        return paymentDetails.length;
    }
}
