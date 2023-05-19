package com.example.zismo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    View v;
    TextView t;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contactos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
             v =inflater.inflate(R.layout.fragment_first, container, false);
             t = v.findViewById(R.id.txtall);
          if(this.CargarPreferenciasNombre("Credenciales0")!="null"){
              for (int i = 0; i < 2 ;i++ ){
                  String nom = CargarPreferenciasNombre("Credenciales"+i);
                  String num = CargarPreferenciasNum("Credenciales"+i);
                  t.append("\n Nombre: "+nom + "\n" + " Numero: "+num + "\n"+"*****************");
              }}



        return v;
    }

    public String CargarPreferenciasNombre(String credenciales){
        SharedPreferences preferencias1 = getActivity().getSharedPreferences(credenciales, Context.MODE_PRIVATE);
        String nom = preferencias1.getString("User","");
        return nom;
    }

    public String CargarPreferenciasNum(String credenciales){
        SharedPreferences preferencias2 = getActivity().getSharedPreferences(credenciales, Context.MODE_PRIVATE);
        String num = preferencias2.getString("Num","");
        return num;
    }

}