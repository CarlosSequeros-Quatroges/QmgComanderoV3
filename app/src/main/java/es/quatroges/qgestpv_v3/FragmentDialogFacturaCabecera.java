package es.quatroges.qgestpv_v3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class FragmentDialogFacturaCabecera extends Fragment {
    static TextView tvEmpresa, tvCIF,tvFormaPago,tvVendedor,tvTPV,tvMESA,tvFactura,
            tvBase,tvTipo,tvIgic,tvTotalEuros,tvEfectivo,tvTarjeta,tvCredito,tvCtaCasa;

    static ImageView ivFirma;
    static FrameLayout flFirma;

    public interface InterfaceFrgDlgFacCabecera {
        Bundle cargaDatos();
    }

    InterfaceFrgDlgFacCabecera interfaceFrgDlgFacCabecera;

    public FragmentDialogFacturaCabecera() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof InterfaceFrgDlgFacCabecera) {
            interfaceFrgDlgFacCabecera = (InterfaceFrgDlgFacCabecera) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_factura_cabecera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle =  interfaceFrgDlgFacCabecera.cargaDatos();
        String empresa = bundle.getString("empresa");
        String cif = bundle.getString("cif");
        String camarero = bundle.getString("camarero");
        String tpv = bundle.getString("tpv");
        String mesa = bundle.getString("mesa");
        String igic = bundle.getString("igic");
        String firma = bundle.getString("firma");

        ClaseFacturaCabecera cab = bundle.getParcelable("cabecera");

        tvEmpresa = view.findViewById(R.id.tvEmpresa);
        tvCIF = view.findViewById(R.id.tvCIF);
        tvFormaPago = view.findViewById(R.id.tvFormapago);
        tvVendedor = view.findViewById(R.id.tvVendedor);
        tvTPV = view.findViewById(R.id.tvTPV);
        tvMESA = view.findViewById(R.id.tvMesa);
        tvFactura = view.findViewById(R.id.tvFactura);
        tvBase = view.findViewById(R.id.tvBase);
        tvTipo = view.findViewById(R.id.tvTipoIgic);
        tvIgic = view.findViewById(R.id.tvIgic);
        tvTotalEuros = view.findViewById(R.id.tvTeuros   );
        tvEfectivo = view.findViewById(R.id.tvEfectivo);
        tvTarjeta = view.findViewById(R.id.tvTarjeta);
        tvCredito = view.findViewById(R.id.tvCredito);
        tvCtaCasa = view.findViewById(R.id.tvCtaCasa);

        flFirma = view.findViewById(R.id.flFirma);
        ivFirma = view.findViewById(R.id.ivFirma);


        tvEmpresa.setText(empresa);
        tvCIF.setText(cif);
        switch (cab.formapago.toLowerCase()) {
            case "e":
                tvFormaPago.setText("Forma de pago: Efectivo");
                break;
            case "h":
                tvFormaPago.setText("Forma de pago: Tarjeta");
                break;
            case "z":
                tvFormaPago.setText("Forma de pago: Efectivo y Tarjeta");
                break;
            case "c":
                tvFormaPago.setText("Forma de pago: Cuenta Casa");
                break;
            case "r":
                tvFormaPago.setText("Forma de pago: Credito");
                break;
        }
        tvVendedor.setText("Camarero: "+camarero);
        tvTPV.setText("TPV: "+tpv);
        tvMESA.setText("Mesa: "+mesa);
        if (cab.nfactura > 0 ) {
            tvFactura.setText("Nº Factura: " + String.valueOf(cab.nfactura));
        }
        else {
            tvFactura.setText("Nº Proforma: " + String.valueOf(-1 * cab.nfactura));
        }

        tvBase.setText(ClaseUtils.double2string((cab.teuros - cab.tigic),2));
        tvTipo.setText(igic);
        tvIgic.setText(ClaseUtils.double2string(cab.tigic,2));
        tvTotalEuros.setText(ClaseUtils.double2string(cab.teuros,2));
        tvEfectivo.setText(ClaseUtils.double2string(cab.efectivo,2));
        tvTarjeta.setText(ClaseUtils.double2string(cab.tarjeta,2));
        tvCredito.setText(ClaseUtils.double2string(cab.credito,2));
        tvCtaCasa.setText(ClaseUtils.double2string(cab.ctacasa,2));

        if (cab.formapago.equalsIgnoreCase("C") && ClaseCondicionesVenta.tipo_cuentacasa.equalsIgnoreCase("G")){
            tvBase.setText(ClaseUtils.double2string(0.00,2));
            tvIgic.setText(ClaseUtils.double2string(0.00,2));
            tvTotalEuros.setText(ClaseUtils.double2string(cab.totalCtaCasa,2));
        }
        if (firma != null && firma.length() > 0){
            byte[] imageAsBytes = Base64.decode(firma.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.length);
            ivFirma.setImageBitmap(bitmap);
            flFirma.setVisibility(View.VISIBLE);

        }
        else {
            flFirma.setVisibility(View.GONE);
        }

    }

    public static FragmentDialogFacturaCabecera newInstance(String text){
        FragmentDialogFacturaCabecera f = new FragmentDialogFacturaCabecera();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}