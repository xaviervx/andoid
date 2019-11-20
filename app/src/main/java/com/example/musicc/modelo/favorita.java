package com.example.musicc.modelo;

public class favorita {
    private int id_usuario;
    private int id_musica;

    public favorita() {
    }

    public favorita(int id_usuario, int id_musica) {
        this.id_usuario = id_usuario;
        this.id_musica = id_musica;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_musica() {
        return id_musica;
    }

    public void setId_musica(int id_musica) {
        this.id_musica = id_musica;
    }
}
