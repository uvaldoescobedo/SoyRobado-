package com.soyrobado.soyrobadoprd.OBJ;

public class Celular {
    String estado,id_user,mac,nombre_dis;

    @Override
    public String toString() {
        return nombre_dis+" ->"+estado;
    }

    public String getId_user() {
        return id_user;
    }

    public Celular() {
    }

    public Celular(String estado, String id_user, String mac, String nombre_dis) {
        this.estado = estado;
        this.id_user = id_user;
        this.mac = mac;
        this.nombre_dis = nombre_dis;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre_dis() {
        return nombre_dis;
    }

    public void setNombre_dis(String nombre_dis) {
        this.nombre_dis = nombre_dis;
    }


}
