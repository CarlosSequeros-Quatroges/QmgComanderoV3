package es.quatroges.qgestpv_v3.datos.listas.lineaVentas;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.GrabaLineasVenta;
import es.quatroges.qgestpv_v3.datos.LineasVenta;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public final class LineaVentaMapper {

    private LineaVentaMapper() {
    }

    @NonNull
    public static ClaseLineaVentas fromRest(@NonNull LineasVenta linea, int cantidadAnterior) {
        ClaseLineaVentas claseLineaVentas = new ClaseLineaVentas(
                linea.getCodigo(),
                linea.getMesa(),
                linea.getSubmesa(),
                linea.getCodmenu(),
                linea.getCantidad(),
                linea.getPeuros(),
                linea.getTeuros(),
                linea.getPcoste(),
                linea.getTcoste(),
                linea.getDescripcion(),
                linea.getCodtpv(),
                linea.getHappy_hour(),
                "",
                ClaseUtils.enEstado.transmitida,
                linea.getCodusu(),
                linea.getTipo(),
                linea.getTmenu(),
                ClaseUtils.enEstado.transmitida,
                linea.getMprecio(),
                linea.getNfactura(),
                "N",
                linea.getOrden_platos(),
                false,
                linea.getDescuento(),
                cantidadAnterior,
                linea.getNlinea(),
                linea.getPension()
        );
        claseLineaVentas.nota = "";
        claseLineaVentas.extras = copyExtras(linea.getExtras());
        return claseLineaVentas;
    }

    @NonNull
    public static GrabaLineasVenta toGraba(@NonNull ClaseLineaVentas linea) {
        GrabaLineasVenta tlinea = new GrabaLineasVenta();
        tlinea.setNlinea(linea.linea_app);
        tlinea.setCodigo(linea.codigo);
        tlinea.setMesa(linea.mesa);
        tlinea.setSubmesa(linea.submesa);
        tlinea.setTmenu(linea.tmenu);
        tlinea.setCodmenu(linea.codmenu);
        tlinea.setCantidad(linea.cantidad);
        tlinea.setCantidad_ant(linea.cantidad_ant);
        tlinea.setPeuros(ClaseUtils.double2string(linea.peuros, 2));
        tlinea.setTeuros(ClaseUtils.double2string(linea.teuros, 2));
        tlinea.setDescripcion(linea.descripcion);
        tlinea.setCodtpv(linea.codtpv);
        tlinea.setHappy_hour(linea.happyhour);
        tlinea.setDescuento(linea.descuento);
        tlinea.setCodusu(linea.codusu);
        tlinea.setTipo(linea.tipo);
        tlinea.setEstado(linea.estado != null ? linea.estado.toString() : "");
        tlinea.setImprimir(linea.imprimir);
        tlinea.setOrden_platos(linea.orden_platos);
        tlinea.setPcoste(ClaseUtils.double2string(linea.pcoste, 2));
        tlinea.setTcoste(ClaseUtils.double2string(linea.tcoste, 2));
        tlinea.setPension(linea.pension);
        tlinea.setExtras(copyExtras(linea.extras));
        return tlinea;
    }

    @NonNull
    public static ArrayList<ClaseItemExtra> copyExtras(ArrayList<ClaseItemExtra> extras) {
        ArrayList<ClaseItemExtra> copia = new ArrayList<>();
        if (extras == null) {
            return copia;
        }
        for (ClaseItemExtra extra : extras) {
            if (extra != null) {
                copia.add(new ClaseItemExtra(extra));
            }
        }
        return copia;
    }
}
