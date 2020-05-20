package com.soyrobado.soyrobadoprd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soyrobado.soyrobadoprd.OBJ.CONSTANTES;
import com.soyrobado.soyrobadoprd.OBJ.Celular;
import com.soyrobado.soyrobadoprd.UI.Error_Fragment;
import com.soyrobado.soyrobadoprd.UI.Info;
import com.soyrobado.soyrobadoprd.UI.Login_Fragment;
import com.soyrobado.soyrobadoprd.UI.Registro_Fragment;
import com.soyrobado.soyrobadoprd.UI.add_device;
import com.soyrobado.soyrobadoprd.UI.edit_device;
import com.soyrobado.soyrobadoprd.UI.list_add;
import com.soyrobado.soyrobadoprd.UI.verificarDispo;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, edit_device.OnFragmentInteractionListener, add_device.OnFragmentInteractionListener,list_add.OnFragmentInteractionListener,Registro_Fragment.OnFragmentInteractionListener, verificarDispo.OnFragmentInteractionListener,Info.OnFragmentInteractionListener, Error_Fragment.OnFragmentInteractionListener,Login_Fragment.OnFragmentInteractionListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnverificar;
    String address;
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(MainActivity.this, "El usuario ya inicio session", Toast.LENGTH_SHORT).show();

                    Toast.makeText(MainActivity.this, user.getUid()+"", Toast.LENGTH_SHORT).show();

                    if (!user.isEmailVerified()) {
                         actualizarMenu(false);
                        Toast.makeText(MainActivity.this, "Correo no Verificado", Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(MainActivity.this).setTitle("Aceptar Correo") // definimos el titulo
                                .setMessage("Se Envio un Correo Verificar y poder Usar la Aplicacion, O Cancela si no") // definimos el mensaje del dialogo
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })
                                .show();

                        user.sendEmailVerification();
                        firebaseAuth.signOut();
                    }else{
                        //aqui poner la siguiente actividad
                        actualizarMenu(true);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "El usuario NO inicio session", Toast.LENGTH_SHORT).show();
                    actualizarMenu(false);
                }



            }


        };



        //fragmentverificar dispo
        Fragment fragment = new verificarDispo();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragment).commit();


         address = getMacAddr();



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




    private void fireStore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "asda");
        user.put("last", "dasd");
        user.put("born", 815);

// Add a new document with a generated ID
        FirebaseUser users = firebaseAuth.getCurrentUser();
        String uID = users.getUid();
        db.collection("users")
                .document(uID)
                .set(user)

                /*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,"Correcto",Toast.LENGTH_SHORT).show();
                         }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                })*/;
    }


    private void InicializarFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }


private void firerealtime(){
    InicializarFireBase();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String uID = user.getUid();


    Celular celularreoprte = new Celular();
    celularreoprte.setId_user(uID);
    celularreoprte.setMac(address);
    celularreoprte.setEstado("Reportado");
    celularreoprte.setNombre_dis("Emulador nexus 5");

        databaseReference.child("Reportes").child(address).setValue(celularreoprte);


}



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        Boolean Seleccionado=false;

        if (id == R.id.IniciarSessionBTN) {
            Toast.makeText(MainActivity.this,"Iniciar Session",Toast.LENGTH_SHORT).show();
            fragment= new Login_Fragment();
            Seleccionado=true;
        } else if (id == R.id.reporteErroBTN_nosession) {
            fragment= new Error_Fragment();
            Seleccionado=true;
            Toast.makeText(MainActivity.this,"Reporte Sin",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Info) {
            Toast.makeText(MainActivity.this,"Informacion",Toast.LENGTH_SHORT).show();
            fragment= new Info();
            Seleccionado=true;
        }else if (id == R.id.MisDispositivos) {
            Toast.makeText(MainActivity.this,"MisDispositivos",Toast.LENGTH_SHORT).show();
           fragment= new list_add();
            Seleccionado=true;

        }else if (id == R.id.navigation_error) {
            Toast.makeText(MainActivity.this,"Verificar este Dispositivo",Toast.LENGTH_SHORT).show();
            fragment= new verificarDispo();
           Seleccionado=true;

        }else if (id == R.id.CerrarSession) {
            Toast.makeText(MainActivity.this,"Cerrar",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }else if (id == R.id.reporteErroBTN_con_session) {
            Toast.makeText(MainActivity.this,"Reporte Con",Toast.LENGTH_SHORT).show();
            fragment= new Error_Fragment();
            Seleccionado=true;

        }

        if(Seleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    NavigationView navigationView;

    private void actualizarMenu(boolean b) {

        if(b){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_con_session);
        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_sin_session);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        btnverificar=findViewById(R.id.button2);
        btnverificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Me dieron Clci", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
