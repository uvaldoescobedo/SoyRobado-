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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.OBJ.Usuario;
import com.soyrobado.soyrobadoprd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link edit_device.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link edit_device#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_device extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public edit_device() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment edit_device.
     */
    // TODO: Rename and change types and number of parameters
    public static edit_device newInstance(String param1, String param2) {
        edit_device fragment = new edit_device();
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

    public void setObj(int i,Usuario usuario){
        this.usuario=usuario;
        this.i=i;
    }
    View view;
    Spinner spinner;
    Usuario usuario;

    EditText nombre;
    Button btn_save;
    int i;
    String [] Opociones  = {"No Reportado","Reportar","Recuperado","Perdido",};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_edit_device, container, false);
        spinner= view.findViewById(R.id.spinner);
        nombre=view.findViewById(R.id.name_actual);
        btn_save=view.findViewById(R.id.btn_save_devicenew);
        nombre.setText(usuario.getCelularesUsuario().get(i).getNombre_dis());



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,Opociones);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinnerdefault();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizaCelular();
            }
        });



        return view;
    }
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    DatabaseReference databaseReference;
    Context context;
    private FirebaseAuth firebaseAuth;
    List<Celular> listanueva;
    private void actualizaCelular() {

            FirebaseApp.initializeApp(context);
            firebaseDatabase= FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            firebaseAuth = FirebaseAuth.getInstance();

            mDatabase = firebaseDatabase.getInstance().getReference();
            //agregar el dispositivo a la lista

            String nombred  =  nombre.getText().toString().trim();
            Celular celularNuevo = new Celular();
            celularNuevo.setId_user(usuario.getId());
            celularNuevo.setMac(usuario.getCelularesUsuario().get(i).getMac());
            celularNuevo.setEstado(spinerslected());
            celularNuevo.setNombre_dis(nombred);

            //remplazamos el actual i oir el nuvevo

             listanueva= new ArrayList<Celular>();

            listanueva=usuario.getCelularesUsuario();
            listanueva.set(i,celularNuevo);

            //enviar usuario a firerbase

            databaseReference.child("Usuarios").child(usuario.getId()).child("celularesUsuario").setValue(listanueva).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    new AlertDialog.Builder(getActivity()).setTitle("Correcto! ") // definimos el titulo
                            .setMessage("Se Actualizo el Estado") // definimos el mensaje del dialogo
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

    private String spinerslected() {

        String estado= spinner.getSelectedItem().toString();
        if(spinner.getSelectedItem().toString().equals("Reportar")){
            estado = "Reportado";
            Reportar();
        }
        if(spinner.getSelectedItem().toString().equals("Perdido")){
            estado = "Reportado";
            Reportar();
        }

        if(spinner.getSelectedItem().toString().equals("No Reportado")){
           eliminarDeReporetes();
        }
        if(spinner.getSelectedItem().toString().equals("Recuperado")){
            eliminarDeReporetes();
        }




        return  estado;
    }

    private void eliminarDeReporetes() {

        databaseReference.child("Reportes").child(usuario.getCelularesUsuario().get(i).getMac()).removeValue();

        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Se ha Recuperado El Dispositivo", Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private void Reportar() {

        Celular celularreoprte = new Celular();
        celularreoprte.setId_user(usuario.getId());
        celularreoprte.setMac(usuario.getCelularesUsuario().get(i).getMac());
        celularreoprte.setEstado("Reportado");
        celularreoprte.setNombre_dis(usuario.getCelularesUsuario().get(i).getNombre_dis());

        databaseReference.child("Reportes").child(usuario.getCelularesUsuario().get(i).getMac()).setValue(celularreoprte);

        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Se ha Reportado El Dispositivo", Snackbar.LENGTH_LONG);
        snackBar.show();

    }

    private void spinnerdefault() {

        if(usuario.getCelularesUsuario().get(i).getEstado().equals("No Reportado")){
            spinner.setSelection(0);
        }else
            if(usuario.getCelularesUsuario().get(i).getEstado().equals("Reportado")){
                Opociones[1]="Reportado";
                spinner.setSelection(1);
        }else
            if(usuario.getCelularesUsuario().get(i).getEstado().equals("Recuperado")){
                spinner.setSelection(2);
        }else
            if(usuario.getCelularesUsuario().get(i).getEstado().equals("Perdido")){
                spinner.setSelection(3);
        }


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
