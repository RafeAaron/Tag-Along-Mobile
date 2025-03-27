package com.example.tagalong.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagalong.R;
import com.example.tagalong.payments.PaymentAdapter;

public class PaymentFragment extends Fragment {

    public View fragmentLayout;
    private Context parentContext;
    public PaymentFragment(Context parentContext) {
        // Required empty public constructor
        this.parentContext = parentContext;
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

        RecyclerView recyclerView = (RecyclerView) layoutProduced.findViewById(R.id.listOfPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.parentContext));
        recyclerView.setAdapter(new PaymentAdapter());

        return layoutProduced;
    }

    public View getView()
    {
        return fragmentLayout;
    }
}