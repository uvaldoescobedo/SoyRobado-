package com.soyrobado.soyrobadoprd;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends Application {
//persistencia de datos para que cuando la app no tenga internet guarde el dato, y cuando se conecte lo suba

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
