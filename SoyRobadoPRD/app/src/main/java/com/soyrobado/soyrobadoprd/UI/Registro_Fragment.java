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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.soyrobado.soyrobadoprd.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Registro_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Registro_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registro_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Registro_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registro_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Registro_Fragment newInstance(String param1, String param2) {
        Registro_Fragment fragment = new Registro_Fragment();
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
    private FirebaseAuth firebaseAuth;
    Button btn_crearUIsuario;
    EditText username,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Vista = inflater.inflate(R.layout.fragment_registro_, container, false);;
        btn_crearUIsuario = Vista.findViewById(R.id.btn_createuser);
        username = Vista.findViewById(R.id.username);
        password=Vista.findViewById(R.id.password);

        btn_crearUIsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearUsuario(username.getText().toString().trim(),password.getText().toString().trim());
            }
        });


        return Vista;
    }

    private void crearUsuario( String usuario, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        final String u=usuario,p=password;

        firebaseAuth.createUserWithEmailAndPassword(u,p).
                addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "Error Al crear Usuario", Snackbar.LENGTH_LONG);
                            snackBar.show();
                        }else{

                            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "Revisa tu bandeja para iniciar session", Snackbar.LENGTH_LONG);
                            snackBar.show();
                            Fragment fragment = new verificarDispo();
                            //limpiar backs
                            // fragment.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contenedor,fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
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
}
