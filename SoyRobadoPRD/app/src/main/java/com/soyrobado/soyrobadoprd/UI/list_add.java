package com.soyrobado.soyrobadoprd.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.OBJ.Usuario;
import com.soyrobado.soyrobadoprd.R;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link list_add.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link list_add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list_add extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public list_add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list_add.
     */
    // TODO: Rename and change types and number of parameters
    public static list_add newInstance(String param1, String param2) {
        list_add fragment = new list_add();
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

    private View Vista;
    String address;
    Button btn_registrar_este;
    ListView listViewCelulares;
    ArrayAdapter<Celular> arrayAdapterceLULARES;
    Celular celularSeleccionado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Vista =  inflater.inflate(R.layout.fragment_list_add, container, false);
        listViewCelulares= Vista.findViewById(R.id.CelularesRegistrados);
        btn_registrar_este= Vista.findViewById(R.id.btn_registrar_este_dis);

       consultarcelualres();

       listViewCelulares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               celularSeleccionado = (Celular) adapterView.getItemAtPosition(i);
               edit_device fragment = new edit_device();

               fragment.setObj(i,usuario);

               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.contenedor,fragment)
                       .addToBackStack(null)
                       .commit();
           }
       });


        btn_registrar_este.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new add_device();
                //limpiar backs
                // fragment.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return Vista;
    }

    String uID;
    FirebaseUser user;
    List<Celular> celularesdeusuario ;
    Usuario usuario;

    private void consultarcelualres() {
        //verificamos si existe el celular en la bd en cualquier usuario

        FirebaseApp.initializeApp(context);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        address=getMacAddr();

        celularesdeusuario = new ArrayList<Celular>();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        uID = user.getUid();


        mDatabase = firebaseDatabase.getInstance().getReference();

        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot objdataSnapshot1: dataSnapshot.getChildren()){
                    Usuario c = objdataSnapshot1.getValue(Usuario.class);
                    List<Celular> list = c.getCelularesUsuario();

                    if(c.getId().equals(uID)){
                        usuario =c;
                        celularesdeusuario=c.getCelularesUsuario();
                        actualizarlista(celularesdeusuario);

                    }
                    for(int i=0; i<list.size();i++){
                        if(list.get(i).getMac().equals(address)){
                            btn_registrar_este.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void actualizarlista(List<Celular> celularesdeusuario) {

        arrayAdapterceLULARES = new ArrayAdapter<Celular>(getActivity().getApplicationContext(),android.R.layout.simple_expandable_list_item_1,celularesdeusuario);
        listViewCelulares.setAdapter(arrayAdapterceLULARES);
    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Context context;


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
