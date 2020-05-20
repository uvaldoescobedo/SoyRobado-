package com.soyrobado.soyrobadoprd.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soyrobado.soyrobadoprd.MainActivity;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.OBJ.Usuario;
import com.soyrobado.soyrobadoprd.R;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link add_device.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link add_device#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_device extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public add_device() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_device.
     */
    // TODO: Rename and change types and number of parameters
    public static add_device newInstance(String param1, String param2) {
        add_device fragment = new add_device();
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
    Button btn_save_new_device;
    EditText nombredis;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Vista=  inflater.inflate(R.layout.fragment_add_device, container, false);

        btn_save_new_device=Vista.findViewById(R.id.btn_save_devicenew);

        nombredis=Vista.findViewById(R.id.edit_namedevice);
        cargarlista();

        btn_save_new_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                agregar_dispositivo();
            }
        });




    return  Vista;
    }

    String uID;
    FirebaseUser user;

    List<Celular> celularesdeusuario ;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;

    private void cargarlista() {

        celularesdeusuario = new ArrayList<Celular>();
        FirebaseApp.initializeApp(context);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        address=getMacAddr();

        firebaseAuth = FirebaseAuth.getInstance();
         user = firebaseAuth.getCurrentUser();

        uID = user.getUid();


        //consultar todos los dispositivos que coincidan con el usuario actual y agreggarlos a la lista de solo este usuario


        mDatabase = firebaseDatabase.getInstance().getReference();

        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // celularesdeusuario.clear();
                for(DataSnapshot objdataSnapshot1: dataSnapshot.getChildren()){
                    Usuario c = objdataSnapshot1.getValue(Usuario.class);
                    if(c.getId().equals(uID)){
                        celularesdeusuario=c.getCelularesUsuario();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void agregar_dispositivo() {

        //agregar el dispositivo a la lista

        String nombre  = nombredis.getText().toString().trim();
        Celular celularNuevo = new Celular();
        celularNuevo.setId_user(uID);
        celularNuevo.setMac(address);
        celularNuevo.setEstado("No Reportado");
        celularNuevo.setNombre_dis(nombre);
        celularesdeusuario.add(celularNuevo);

        Usuario u = new Usuario(user.getEmail(),uID,celularesdeusuario);
        //enviar usuario a firerbase

        databaseReference.child("Usuarios").child(uID).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                new AlertDialog.Builder(getActivity()).setTitle("Correcto! ") // definimos el titulo
                        .setMessage("Se Registro Este Dispositivo") // definimos el mensaje del dialogo
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment = new list_add();
                                //limpiar backs
                                // fragment.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor,fragment)
                                        .addToBackStack(null)
                                        .commit();

                            }
                        })
                        .show();
            }
        });


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
}
