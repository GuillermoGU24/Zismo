package com.example.zismo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import java.io.IOException;
import java.net.URI;
import java.util.Arrays;


public class HomeFragment extends Fragment {

    String address;
    String info;
    View v;
    TextView text3;
    String bt;

    public static final String ACCOUNT_SID = "ACec7180b17b6142c6ef80bc20f23c3f75";
    public static final String AUTH_TOKEN = "e60fbf8d1be288a93beda7f7dc37a791";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Zimo");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getActivity().getIntent().getExtras();
        bt = bundle.getString(MainActivity.vINFO);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        text3 = (TextView) v.findViewById(R.id.textView3);

        info = getArguments().getString("Info");
        address = getArguments().getString("Direc");
        text3.setText(info);

        return v;
    }

}