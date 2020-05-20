package com.soyrobado.soyrobadoprd.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soyrobado.soyrobadoprd.MainActivity;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Error_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Error_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Error_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Error_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Error_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Error_Fragment newInstance(String param1, String param2) {
        Error_Fragment fragment = new Error_Fragment();
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
    Button enviar_reporte;
    EditText mail_report,report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        Vista= inflater.inflate(R.layout.fragment_error_, container, false);

        enviar_reporte = Vista.findViewById(R.id.send_report);
        mail_report=Vista.findViewById(R.id.error_mail);
        report=Vista.findViewById(R.id.error_error);


        enviar_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "clic", Snackbar.LENGTH_LONG);
                snackBar.show();
                new AlertDialog.Builder(getActivity()).setTitle("Informar Error ") // definimos el titulo
                        .setMessage("Hola lamentamos que tengas incovenientes, en breve te responderemos a tu correo  Un Saludo!") // definimos el mensaje del dialogo
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                enviarReporte(mail_report.getText().toString(),report.getText().toString());
                                Fragment fragment = new verificarDispo();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contenedor,fragment)
                                        .addToBackStack(null)
                                        .commit();

                            }
                        })
                        .show();


            }
        });




        return  Vista;
    }



    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Context context;
    private void enviarReporte(String mail, String error) {

        FirebaseApp.initializeApp(context);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

            String user_que_reporta="";
        if(user==null){
            user_que_reporta= UUID.randomUUID().toString();
        }else{
            user_que_reporta=user.getUid()+"***"+UUID.randomUUID().toString();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("Correo", mail);
        data.put("Id_User", user_que_reporta);
        data.put("Error_Reportado", error);


        databaseReference.child("Errores").child(user_que_reporta).setValue(data);



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
