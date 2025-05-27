package com.example.tagalong.Fragments;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.example.tagalong.home.HomeMain;
import com.example.tagalong.payments.PaymentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentFragment extends Fragment {

    public View fragmentLayout;
    private Context parentContext;

    JSONObject paymentDetails;

    JSONObject jsonPaymentDetails;
    public PaymentFragment(Context parentContext, JSONObject json) {
        // Required empty public constructor
        this.parentContext = parentContext;
        this.jsonPaymentDetails = json;

        try {
            this.paymentDetails = getAccountDetails(parseInt(json.getJSONObject("User").get("id").toString()));
        } catch (JSONException e) {
            Log.e("Error", "Failed to rejoin threads: " + e.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutProduced = inflater.inflate(R.layout.fragment_payment, container, false);
        fragmentLayout = layoutProduced;

        TextView amountInWallet = layoutProduced.findViewById(R.id.amountInWallet);
        try {

            if(this.paymentDetails.has("amount")) {
                amountInWallet.setText("UGX " + this.paymentDetails.get("amount").toString());
            }else{
                amountInWallet.setText("N/A");
            }

            RecyclerView recyclerView = (RecyclerView) layoutProduced.findViewById(R.id.listOfPayments);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.parentContext));
            recyclerView.setAdapter(new PaymentAdapter(getPayments(parseInt(this.paymentDetails.get("userID").toString()))));
        } catch (JSONException e) {
            Log.e("Error", "Failed to rejoin threads: " + e.toString());
        }

        return layoutProduced;
    }

    public View getView()
    {
        return fragmentLayout;
    }

    public JSONObject getAccountDetails(int userId)
    {

        final JSONObject[] accountDetails = {new JSONObject()};

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {

                NetworkConnection conn = new NetworkConnection();

                JSONObject req = new JSONObject();
                try {
                    req.put("user_id", userId);
                    if(conn.checkConnection())
                    {
                        JSONObject res = conn.postData(req, "getUserAccount");
                        if(res.has("Account"))
                        {
                            accountDetails[0] = (JSONObject) res.get("Account");
                        }

                    }

                } catch (JSONException e) {
                    Log.e("Error", "Failed to parse JSON: " + e.toString());
                }

            }
        });

        newThread.start();

        try {
            newThread.join();
        } catch (InterruptedException e) {
            Log.e("Error", "Failed to rejoin threads: " + e.toString());
        }

        return accountDetails[0];

    }

    public JSONObject getPayments(int userId)
    {

        final JSONObject[] json = new JSONObject[1];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject reqData = new JSONObject();

                NetworkConnection conn = new NetworkConnection();

                try {
                    reqData.put("user_id", userId);
                    JSONObject res = conn.getData(reqData, "getPaymentsForUser");

                    json[0] = res;

                } catch (JSONException e) {
                    Log.e("Error", "Failed to parse JSON: " + e.toString());
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error", "Failed to rejoin threads: " + e.toString());
        }

        return json[0];

    }
}