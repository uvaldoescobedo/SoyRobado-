package com.soyrobado.soyrobadoprd.UI;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.soyrobado.soyrobadoprd.MainActivity;
import  com.soyrobado.soyrobadoprd.OBJ.CONSTANTES;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soyrobado.soyrobadoprd.OBJ.CONSTANTES;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.R;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link verificarDispo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link verificarDispo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class verificarDispo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public verificarDispo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment verificarDispo.
     */
    // TODO: Rename and change types and number of parameters
    public static verificarDispo newInstance(String param1, String param2) {
        verificarDispo fragment = new verificarDispo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View Vista;
    Button btnverificar;
    ImageView imagenestado;
    TextView textestado;
    int con;
    Context context;
    FirebaseFirestore db;
    String estado="";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    boolean exisrepot=false;

    List<Celular> celularesreportador = new ArrayList<Celular>();

    private void firerConsulta(){
        FirebaseApp.initializeApp(context);

        final String address = getMacAddr();

        mDatabase = firebaseDatabase.getInstance().getReference();

        mDatabase.child("Reportes").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exisrepot=false;
                celularesreportador.clear();
                for(DataSnapshot objdataSnapshot1: dataSnapshot.getChildren()){
                    Celular c = objdataSnapshot1.getValue(Celular.class);
                    if((c.getMac()).equals(address)) {
                        exisrepot=true;
                        celularesreportador.add(c);
                    }
                }

                if(exisrepot) {
                    imagenestado.setImageResource(0);
                    imagenestado.destroyDrawingCache();
                    imagenestado.requestLayout();
                    imagenestado.setImageResource(R.drawable.tache);
                    textestado.setText(" REPORTADO ");
                }else{
                    imagenestado.setImageResource(0);
                    imagenestado.destroyDrawingCache();
                    imagenestado.requestLayout();
                    imagenestado.setImageResource(R.drawable.paloma);
                    textestado.setText("Sin Reporte ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Vista = inflater.inflate(R.layout.fragment_verificar_dispo, container, false);


        btnverificar=Vista.findViewById(R.id.button2);
        imagenestado= Vista.findViewById(R.id.imageView);
        imagenestado.setImageResource(R.drawable.antifas);
        textestado=Vista.findViewById(R.id.setEstado);
        con =0;



        btnverificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firerConsulta();


                con++;
               /* if(con==1){


                    firerConsulta();

                }/*
                if(con==2){


                }
                if(con==3){
                    imagenestado.setImageResource(R.drawable.exclamasion);
                    textestado.setText("No Reconocemos Este DisPositivo ");
                }
                if(con==4){
                    imagenestado.setImageResource(R.drawable.interrogacion);
                    textestado.setText(" Sin Reporte Sin Embargo le Pertenece a otra persona");
                    con=0;
                }
*/

            }
        });


        return Vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
