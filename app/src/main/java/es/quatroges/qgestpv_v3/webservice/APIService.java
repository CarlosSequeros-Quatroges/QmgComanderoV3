package es.quatroges.qgestpv_v3.webservice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("comanderoV3/testJSON")
    Call<RespuestaTestjsonWS> recuperaTestJSON();

    @POST("comanderoV3/validar?")
    Call<RespuestaValidarWS> validar(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet);

    @POST("comanderoV3/validarUpdate?")
    Call<RespuestaValidarUpdateWS> validarUpdate(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("appcode") String appcode);

    @GET("comanderoV3/recuperaRegistrosMD5?")
    Call<RespuestaRegistrosMD5WS> recuperaRegistrosMD5(@Query("id") String id,  @Query("codemp") String codemp, @Query("tabla") String tabla, @Query("offset") int offset);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaUsuariosWS> recuperaUsuarios(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaTPVsWS> recuperaTPVS(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaNomMesasWS> recuperaNomMesas(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaAlergenosWS> recuperaAlergenos(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaEtiquetasWS> recuperaEtiquetas(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaClientesCtaCasaWS> recuperaClientesCtaCasa(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaCabecerasWS> recuperaCabeceras(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaProductosWS> recuperaProductos(@Body RequestRecuperaRegistrosWS request);

    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaFamiliasWS> recuperaFamilias(@Body RequestRecuperaRegistrosWS request);
    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaSubFamiliasWS> recuperaSubfamilias(@Body RequestRecuperaRegistrosWS request);
    @POST("comanderoV3/recuperaTabla")
    Call<RespuestaHoraComidasWS> recuperaHoraComidas(@Body RequestRecuperaRegistrosWS request);



    @POST("comanderoV3/recuperaEstadoMesas?")
    Call<RespuestaEstadoMesasWS> recuperaEstadoMesas(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("tpv") String tpv);

    @POST("comanderoV3/recuperaLineasMesa?")
    Call<RespuestaLineasMesaWS> recuperaLineasMesa(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("tpv") String tpv, @Query("pmesa") String mesa, @Query("room") String room );

    @POST("comanderoV3/recuperaPension?")
    Call<RespuestaPensionWS> recuperaPension(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("room") String room, @Query("codenl") String codenl );

    @POST("comanderoV3/grabaLineas?")
    Call<RespuestaGrabaLineasWS> grabaLineas(@Body RequestGrabaLineasWS request);

    @POST("comanderoV3/pedirCuenta?")
    Call<RespuestaPedirCuentaWS> pedirCuenta(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("tpv") String tpv, @Query("pmesa") String mesa,@Query("psubmesa") String submesa,@Query("nfactura") String nfactura ,@Query("codusu") String codusu,@Query("imprimir") String imprimir);

    @POST("comanderoV3/actualizaPax?")
    Call<RespuestaBaseWS> actualizaPax(@Body RequestActualizaPaxWS request);

    @POST("comanderoV3/traspasaMesa?")
    Call<RespuestaBaseWS> traspasaMesa(@Body RequestTraspasaMesaWS request);

    @POST("comanderoV3/pagaMesa?")
    Call<RespuestaPagaMesaWS> pagaMesa(@Body RequestPagaMesaWS request);

    @POST("comanderoV3/recuperaCreditoTagID?")
    Call<RespuestaCreditoWS> recuperaCreditoTagID(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("tagID") String tagID, @Query("codigoEmpresa_ext") String codemp_ext);

    @POST("comanderoV3/recuperaCreditoRoom?")
    Call<RespuestaCreditoWS> recuperaCreditoRoom(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("room") String room, @Query("codigoEmpresa_ext") String codemp_ext);

    @POST("comanderoV3/avisoSeguirPlato?")
    Call<RespuestaBaseWS> avisoSeguirPlato(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("tpv") String tpv, @Query("pmesa") String mesa, @Query("orden") String orden, @Query ("pax") String pax );

    @POST("comanderoV3/recuperaFacturas?")
    Call<RespuestaFacturasWS> recuperaFacturas(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("codtpv") String codtpv);

    @POST("comanderoV3/recuperaFactura?")
    Call<RespuestaFacturaWS> recuperaFactura(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("codenl") String codenl);

    @POST("comanderoV3/imprimeFactura?")
    Call<RespuestaBaseWS> imprimeFactura(@Query("codigoEmpresa") String codigoEmpresa, @Query("IDTablet") String idTablet, @Query("codenl") String codenl);

}
