package com.example.zismo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    int i =0;
    View v;
    EditText nombre;
    EditText numero;
    Button guardar;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Añadir Contacto");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_second, container, false);
        guardar = (Button) v.findViewById(R.id.button);
        nombre = (EditText) v.findViewById((R.id.nombre));
        numero = (EditText) v.findViewById((R.id.numero));

      guardar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(i >=2){
                  Toast.makeText(getContext(),"Limite de contactos",Toast.LENGTH_LONG).show();
                  nombre.setEnabled(false);
                  numero.setEnabled(false);
                  nombre.setText("");
                  numero.setText("");
                  i=0;
              }else {
                  guardarContactoNombre();
                  guardarContactoNumero();
                  i++;
                  Toast.makeText(getContext(), "Contacto Guardado", Toast.LENGTH_LONG).show();
                  nombre.setText("");
                  numero.setText("");
              }

          }
      });

        return v;
    }
    public void guardarContactoNombre(){

        SharedPreferences preferencias1 = getActivity().getSharedPreferences("Credenciales"+i, Context.MODE_PRIVATE);
        String nom =  nombre.getText().toString();

        SharedPreferences.Editor editor1 = preferencias1.edit();
        editor1.putString("User",nom);
        editor1.commit();

    }
    public void guardarContactoNumero(){

        SharedPreferences preferencias2 = getActivity().getSharedPreferences("Credenciales"+i, Context.MODE_PRIVATE);
        String num =  numero.getText().toString();
        SharedPreferences.Editor editor2 = preferencias2.edit();
        editor2.putString("Num",num);
        editor2.commit();

    }


}

