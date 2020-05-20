package com.soyrobado.soyrobadoprd.OBJ;

import java.util.List;

public class Usuario {

    String nombre,id;
    List<Celular> celularesUsuario;

    public Usuario() {
    }

    public Usuario(String nombre, String id, List<Celular> celularesUsuario) {
        this.nombre = nombre;
        this.id = id;
        this.celularesUsuario = celularesUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Celular> getCelularesUsuario() {
        return celularesUsuario;
    }

    public void setNewCelularesUsuario(Celular celularesUsuario) {
        this.celularesUsuario.add(celularesUsuario);
    }
    public void setCelularesUsuario(List<Celular> celularesUsuario) {
        this.celularesUsuario= celularesUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", celularesUsuario=" + celularesUsuario +
                '}';
    }
}
