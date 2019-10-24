package br.tap.filmes;

import java.io.Serializable;

public class Filme implements Serializable {

    private int id;
    private String titulo;
    private String subtitulo;
    private String genero;
    private float avaliacao;

    public Filme() {
        this(0,"<sem título>","<sem subtítulo>","<não informado>",0f);
    }

    public Filme(int id, String titulo, String subtitulo, String genero, float avaliacao) {
        this.setId(id);
        this.setTitulo(titulo);
        this.setSubtitulo(subtitulo);
        this.setGenero(genero);
        this.setAvaliacao(avaliacao);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }
    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public float getAvaliacao() {
        return avaliacao;
    }
    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        String msg = new String();
        msg += titulo + "\n";
        msg += subtitulo + "\n";
        msg += "(" + genero + ") : " + avaliacao;
        return msg;
    }
}
