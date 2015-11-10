package model;

/**
 * Created by User on 09/11/2015.
 */
public class Despacho {

    int idDespacho;
    DetalleFacturaId detalleFacturaId;
    double cantidadComprada;
    double precioVenta;
    boolean yaSeEntrego;

    public Despacho(int idDespacho, DetalleFacturaId detalleFacturaId, double cantidadComprada, double precioVenta, boolean yaSeEntrego) {
        this.idDespacho = idDespacho;
        this.detalleFacturaId = detalleFacturaId;
        this.cantidadComprada = cantidadComprada;
        this.precioVenta = precioVenta;
        this.yaSeEntrego = yaSeEntrego;
    }

    public int getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(int idDespacho) {
        this.idDespacho = idDespacho;
    }

    public DetalleFacturaId getDetalleFacturaId() {
        return detalleFacturaId;
    }

    public void setDetalleFacturaId(DetalleFacturaId detalleFacturaId) {
        this.detalleFacturaId = detalleFacturaId;
    }

    public double getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(double cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public boolean isYaSeEntrego() {
        return yaSeEntrego;
    }

    public void setYaSeEntrego(boolean yaSeEntrego) {
        this.yaSeEntrego = yaSeEntrego;
    }
}
