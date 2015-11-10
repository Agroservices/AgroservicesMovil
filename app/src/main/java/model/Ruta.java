package model;

import java.util.Date;

/**
 * Created by User on 10/11/2015.
 */
public class Ruta {

    int idRutas;
    Date fechaInicio;
    Date fechaFinalizacion;

    public Ruta(int idRutas, Date fechaInicio, Date fechaFinalizacion) {
        this.idRutas = idRutas;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getIdRutas() {
        return idRutas;
    }

    public void setIdRutas(int idRutas) {
        this.idRutas = idRutas;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
