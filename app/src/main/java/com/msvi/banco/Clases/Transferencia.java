package com.msvi.banco.Clases;

public class Transferencia
{
    private String nrotransacc;
    private String nrocuentaorigen;
    private String nrocuentadestino;
    private String fecha;
    private String hora;
    private String valor;

    public String getNrotransacc() {
        return nrotransacc;
    }

    public void setNrotransacc(String nrotransacc) {
        this.nrotransacc = nrotransacc;
    }

    public String getNrocuentaorigen() {
        return nrocuentaorigen;
    }

    public void setNrocuentaorigen(String nrocuentaorigen) {
        this.nrocuentaorigen = nrocuentaorigen;
    }

    public String getNrocuentadestino() {
        return nrocuentadestino;
    }

    public void setNrocuentadestino(String nrocuentadestino) {
        this.nrocuentadestino = nrocuentadestino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
