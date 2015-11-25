package model;

import java.io.Serializable;

/**
 * Created by User on 09/11/2015.
 */
public class Despacho implements Serializable {

    int idDespacho;
    String nombreProducto;
    double cantidadComprada;
    Ubicacion coordenadasEntrega;
    String direccionEntrega;
    String ciudadEntrega;
    Ubicacion coordenadasRecogida;
    String direccionRecogida;
    String ciudadRecogida;
    boolean yaSeEntrego;
    boolean yaSeRecogio;

    public Despacho(){

    }

    public int getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(int idDespacho) {
        this.idDespacho = idDespacho;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(double cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public Ubicacion getCoordenadasEntrega() {
        return coordenadasEntrega;
    }

    public void setCoordenadasEntrega(Ubicacion coordenadasEntrega) {
        this.coordenadasEntrega = coordenadasEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCiudadEntrega() {
        return ciudadEntrega;
    }

    public void setCiudadEntrega(String ciudadEntrega) {
        this.ciudadEntrega = ciudadEntrega;
    }

    public Ubicacion getCoordenadasRecogida() {
        return coordenadasRecogida;
    }

    public void setCoordenadasRecogida(Ubicacion coordenadasRecogida) {
        this.coordenadasRecogida = coordenadasRecogida;
    }

    public String getDireccionRecogida() {
        return direccionRecogida;
    }

    public void setDireccionRecogida(String direccionRecogida) {
        this.direccionRecogida = direccionRecogida;
    }

    public String getCiudadRecogida() {
        return ciudadRecogida;
    }

    public void setCiudadRecogida(String ciudadRecogida) {
        this.ciudadRecogida = ciudadRecogida;
    }

    public boolean isYaSeEntrego() {
        return yaSeEntrego;
    }

    public void setYaSeEntrego(boolean yaSeEntrego) {
        this.yaSeEntrego = yaSeEntrego;
    }

    public boolean isYaSeRecogio() {
        return yaSeRecogio;
    }

    public void setYaSeRecogio(boolean yaSeRecogio) {
        this.yaSeRecogio = yaSeRecogio;
    }

    @Override
    public String toString() {
        return "Despacho{" +
                "idDespacho=" + idDespacho +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidadComprada=" + cantidadComprada +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", ciudadEntrega='" + ciudadEntrega + '\'' +
                ", direccionRecogida='" + direccionRecogida + '\'' +
                ", ciudadRecogida='" + ciudadRecogida + '\'' +
                ", coordenadasRecogida=" + coordenadasRecogida +
                ", coordenadasEntrega=" + coordenadasEntrega +
                ", yaSeEntrego=" + yaSeEntrego +
                ", yaSeRecogio=" + yaSeRecogio +
                '}';
    }
}
