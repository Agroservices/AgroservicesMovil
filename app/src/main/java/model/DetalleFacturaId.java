package model;

/**
 * Created by User on 09/11/2015.
 */
public class DetalleFacturaId {

    int productosEnVentaId;
    int facturasId;

    public DetalleFacturaId(int productosEnVentaId,int facturasId){
        this.productosEnVentaId = productosEnVentaId;
        this.facturasId = facturasId;
    }

    public int getProductosEnVentaId() {
        return productosEnVentaId;
    }

    public void setProductosEnVentaId(int productosEnVentaId) {
        this.productosEnVentaId = productosEnVentaId;
    }

    public int getFacturasId() {
        return facturasId;
    }

    public void setFacturasId(int facturasId) {
        this.facturasId = facturasId;
    }
}
