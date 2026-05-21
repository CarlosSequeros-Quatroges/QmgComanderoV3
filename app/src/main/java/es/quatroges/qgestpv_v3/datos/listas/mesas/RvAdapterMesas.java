package es.quatroges.qgestpv_v3.datos.listas.mesas;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentMesas;
import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterMesas extends RecyclerView.Adapter<RvAdapterMesas.CategoriaViewHolder>
implements Filterable {



    private  FragmentMesas.InterfaceMesas interfaceMesas;

    static ArrayList<ClaseMesas> listaMesas, listaMesasFiltradas;
    Context context;
    FragmentMesas fragmentMesas;
    static View focusView;
    static Activity focusActivity;
    boolean quitaFocoSvMesa;
    static int mesaSel;

    public void setQuitaFocoSvMesa(boolean quitaFocoSvMesa) {
        this.quitaFocoSvMesa = quitaFocoSvMesa;
    }

    public RvAdapterMesas(Context context, ArrayList<ClaseMesas> mesas, FragmentMesas fragmentMesas){
        this.context = context;
        this.listaMesas = null;
        this.listaMesasFiltradas = null;
        if (mesas != null) {
            this.listaMesas = new ArrayList<ClaseMesas>(mesas);
            this.listaMesasFiltradas = new ArrayList<ClaseMesas>(mesas);

        }

        this.interfaceMesas = (FragmentMesas.InterfaceMesas)context;
        this.fragmentMesas = fragmentMesas;
        this.quitaFocoSvMesa = false;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaMesasFiltradas= listaMesas;
                } else {
                    ArrayList<ClaseMesas> filteredList = new ArrayList<>();
                    for (ClaseMesas row : listaMesas) {
                        if(String.valueOf(row.mesa).contains(charString) ||
                                String.valueOf(row.descripcion).toUpperCase().contains(charString.toUpperCase())){
                            filteredList.add(row);
                        }
                    }

                    listaMesasFiltradas= filteredList;
  
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaMesasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                boolean ok = true;
                if (filterResults.values instanceof ArrayList<?>){
                    for (ClaseMesas t:  (ArrayList<ClaseMesas>) filterResults.values){
                        if (t instanceof  ClaseMesas){

                        }
                        else {
                            ok = false;
                            break;
                        }
                    }
                }
                if (ok ) {
                    listaMesasFiltradas = (ArrayList<ClaseMesas>) filterResults.values;

                    notifyDataSetChanged();
                    if (quitaFocoSvMesa)
                        fragmentMesas.swMesaClearFocus();

                    quitaFocoSvMesa = false;
                }

            }
        };
    }

    @Override
    public int getItemCount() {
        if (listaMesasFiltradas != null )
            return listaMesasFiltradas.size();
        else
            return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mesas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, fragmentMesas.getActivity());

        return pvh;
    }

   
    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder categoriaViewHolder, @SuppressLint("RecyclerView") final int i) {
        
        if (listaMesasFiltradas.get(i).abiertas == false && listaMesasFiltradas.get(i).tickets == false ){
            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaLibre);
            categoriaViewHolder.numero.setText(String.valueOf(listaMesasFiltradas.get(i).mesa));
            categoriaViewHolder.descripcion.setText(listaMesasFiltradas.get(i).descripcion);
            categoriaViewHolder.pax.setText("");
            categoriaViewHolder.room.setText("");
            categoriaViewHolder.importe.setText("");
            categoriaViewHolder.etPax.setText("");
            categoriaViewHolder.etRoom.setText("");
            categoriaViewHolder.ivMesa.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivPax.setVisibility(View.INVISIBLE);
            categoriaViewHolder.ivEstado.setVisibility(View.INVISIBLE);
            categoriaViewHolder.ivRoom.setVisibility(View.INVISIBLE);
        }
        else if (listaMesasFiltradas.get(i).abiertas == true||
                listaMesasFiltradas.get(i).tickets == true) {

            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaOcupada);
            categoriaViewHolder.numero.setText(String.valueOf(listaMesasFiltradas.get(i).mesa));
            categoriaViewHolder.descripcion.setText(listaMesasFiltradas.get(i).descripcion);
            categoriaViewHolder.pax.setText(String.valueOf(listaMesasFiltradas.get(i).pax));
            categoriaViewHolder.etPax.setText(Integer.toString(listaMesasFiltradas.get(i).pax));
            categoriaViewHolder.ivMesa.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivPax.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivRoom.setVisibility(VISIBLE);
            categoriaViewHolder.ivEstado.setVisibility(View.INVISIBLE);

            if (listaMesasFiltradas.get(i).tickets == true)
                categoriaViewHolder.ivEstado.setVisibility(View.VISIBLE);

            categoriaViewHolder.room.setVisibility(View.INVISIBLE);
            categoriaViewHolder.etRoom.setText("");
            if (listaMesasFiltradas.get(i).submesas.size() > 0 ){
                categoriaViewHolder.room.setVisibility(VISIBLE);
                String rooms = "";
                for (ClaseSubMesas submesa :listaMesasFiltradas.get(i).submesas) {
                    if (submesa.habitacion == null || submesa.habitacion.trim().equals("")){
                        continue;
                    }

                    if (! rooms.equals("")) {
                        rooms += "\n";
                    }
                    rooms += submesa.habitacion;
                }
                categoriaViewHolder.room.setText(rooms);
                categoriaViewHolder.etRoom.setText(listaMesasFiltradas.get(i).submesas.get(0).habitacion);
            }

            categoriaViewHolder.importe.setText(ClaseUtils.double2string(listaMesasFiltradas.get(i).importe,2));


        }

        if (fragmentMesas.mesaSel == listaMesasFiltradas.get(i).mesa){
            categoriaViewHolder.laySel.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.laySel.setBackgroundResource(R.color.cvBorde);
        }

        categoriaViewHolder.cv.setAlpha((float) 1.0);
        categoriaViewHolder.layDatos.setVisibility(View.GONE);

        if (fragmentMesas.estado == FragmentMesas.enEstado.datos && fragmentMesas.mesaSel != listaMesasFiltradas.get(i).mesa) {
            categoriaViewHolder.cv.setAlpha((float) 0.2);
        }
        else if (fragmentMesas.estado == FragmentMesas.enEstado.datos && fragmentMesas.mesaSel == listaMesasFiltradas.get(i).mesa) {
            categoriaViewHolder.layDatos.setVisibility(View.VISIBLE);
        }

        categoriaViewHolder.fondo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (fragmentMesas.estado == FragmentMesas.enEstado.seleccion &&
                        (listaMesasFiltradas.get(i).abiertas ==true ||
                         listaMesasFiltradas.get(i).tickets== true )){

                    fragmentMesas.mesaSel = listaMesasFiltradas.get(i).mesa;
                    /*
                    categoriaViewHolder.layDatos.setVisibility(View.VISIBLE);
                    fragmentMesas.estado = FragmentMesas.enEstado.datos;
                    categoriaViewHolder.etPax.setText(Integer.toString(listaMesasFiltradas.get(i).pax));
                    categoriaViewHolder.etPax.setHint(context.getResources().getString(R.string.hint_pax));
                    categoriaViewHolder.etPax.requestFocus();
                    notifyDataSetChanged();
                    */

                    mesaSel = i;
                    //cuando hago click largo sobre la mesa en vista estado de mesas.
                    //OK
                    ClaseUtils.PidePaxSubmesa aviso =  new ClaseUtils.PidePaxSubmesa();
                    aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                    aviso.setMensaje(context.getResources().getString(R.string.dialog_strTituloPidePax));
                    aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                    aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                    aviso.setOnclick(onLongClickPidePax);
                    aviso.setTipo(ClaseUtils.PidePaxSubmesa.TIPO_DIALOGO_ORGANIZA_PAX);

                    aviso.setContext(context);
                    aviso.setSubmesas(listaMesasFiltradas.get(i).submesas);
                    aviso.setMover_uds(false);
                    aviso.execute(0);
                }
                return true;
            }
        });

        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentMesas.estado == FragmentMesas.enEstado.seleccion &&
                        listaMesasFiltradas.get(i).abiertas == false &&
                        listaMesasFiltradas.get(i).tickets == false) {      /// mesa libre

                    fragmentMesas.mesaSel = listaMesasFiltradas.get(i).mesa;
                    if (fragmentMesas.pidePax == true || fragmentMesas.pideRoom == true) { // pedir pax = SI
                        fragmentMesas.estado = FragmentMesas.enEstado.datos;
                        categoriaViewHolder.etPax.setHint(context.getResources().getString(R.string.hint_pax));

                        if (categoriaViewHolder.etPax.isFocused())
                            categoriaViewHolder.etPax.clearFocus();

                        fragmentMesas.filtrarItem(listaMesasFiltradas.get(i).descripcion);
                        categoriaViewHolder.etPax.requestFocus();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                categoriaViewHolder.layDatos.setVisibility(View.VISIBLE);
                            }
                        },200);

                    }
                    else { // no pedir pax
                        fragmentMesas.estado = FragmentMesas.enEstado.seleccion;
                        categoriaViewHolder.layDatos.setVisibility(View.GONE);
                        notifyDataSetChanged();
                        String tmpPax = Integer.toString(listaMesasFiltradas.get(i).pax);
                        interfaceMesas.onClickMesa(listaMesasFiltradas.get(i).mesa,Integer.valueOf(tmpPax),"");
                    }

                }
                else if (fragmentMesas.estado == FragmentMesas.enEstado.seleccion &&
                        (listaMesasFiltradas.get(i).abiertas ==true || listaMesasFiltradas.get(i).tickets == true)) { // mesa ocupada

                    fragmentMesas.estado = FragmentMesas.enEstado.seleccion;
                    categoriaViewHolder.layDatos.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    String tmpPax = Integer.toString(listaMesasFiltradas.get(i).pax);

                    String tmpRoom ="";
                    if (listaMesasFiltradas.get(i).submesas.size() > 0 ){
                        tmpRoom = listaMesasFiltradas.get(i).submesas.get(0).habitacion;
                    }
                    interfaceMesas.onClickMesa(listaMesasFiltradas.get(i).mesa, Integer.valueOf(tmpPax),tmpRoom);

                }
            }
        });

        categoriaViewHolder.fondo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listaMesasFiltradas.get(i).tickets == true){
                    interfaceMesas.onLongClickMesa(listaMesasFiltradas.get(i).mesa);
                    return  true;
                }
                return false;
            }
        });

        categoriaViewHolder.etPax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkAceptar(categoriaViewHolder,i);

                    return true;
                }
                return false;
            }
        });

        categoriaViewHolder.etRoom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkAceptar(categoriaViewHolder,i);

                    return true;
                }
                return false;
            }
        });

        categoriaViewHolder.ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAceptar(categoriaViewHolder,i);
               categoriaViewHolder.etPax.clearFocus();

            }
        });

        categoriaViewHolder.ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentMesas.estado == FragmentMesas.enEstado.datos){
                    fragmentMesas.estado = FragmentMesas.enEstado.seleccion;
                    categoriaViewHolder.layDatos.setVisibility(View.GONE);
                    fragmentMesas.mesaSel = -1;
                    fragmentMesas.filtrarItem("");
                    notifyDataSetChanged();
                }
                InputMethodManager imm = (InputMethodManager) focusActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                categoriaViewHolder.etPax.clearFocus();

            }
        });

        categoriaViewHolder.ivExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriaViewHolder.etRoom.setText("EXT");
            }
        });
    }

    private void checkAceptar(RvAdapterMesas.CategoriaViewHolder categoriaViewHolder, int i){
        if (fragmentMesas.estado == FragmentMesas.enEstado.datos){
            String tmpPax = categoriaViewHolder.etPax.getText().toString().trim();
            String tmpRoom = categoriaViewHolder.etRoom.getText().toString().trim();

            if (tmpPax.trim().equals("") || Integer.valueOf(tmpPax.trim()) < 1  || Integer.valueOf(tmpPax.trim()) >99 ) {
                /*
                categoriaViewHolder.etPax.setText("");
                categoriaViewHolder.etPax.setHint(context.getResources().getString(R.string.hint_pax));
                categoriaViewHolder.etPax.requestFocus();
                 */
                categoriaViewHolder.etPax.setText("1");
                tmpPax = "1";
            }

            if (tmpRoom.trim().equals("")) {
                categoriaViewHolder.etRoom.setText("");
                categoriaViewHolder.etRoom.setHint(context.getResources().getString(R.string.hint_room));
                categoriaViewHolder.etRoom.requestFocus();
                return;

            }

            fragmentMesas.estado = FragmentMesas.enEstado.seleccion;
            categoriaViewHolder.layDatos.setVisibility(View.GONE);
            notifyDataSetChanged();
            interfaceMesas.onClickMesa(listaMesasFiltradas.get(i).mesa,Integer.valueOf(tmpPax), tmpRoom);

            InputMethodManager imm = (InputMethodManager) focusActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView numero;
        TextView descripcion;
        TextView pax, room ;

        TextView importe;
        LinearLayout fondo,laySel, layDatos;
        EditText etPax, etRoom;
        androidx.appcompat.widget.AppCompatImageView ivAceptar,ivCancelar, ivEstado, ivMesa, ivPax, ivRoom, ivExterno;


        CategoriaViewHolder(View itemView,final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            numero = itemView.findViewById(R.id.numero);
            descripcion = itemView.findViewById(R.id.descripcion);
            pax = itemView.findViewById(R.id.pax);
            room = itemView.findViewById(R.id.room);
            importe= itemView.findViewById(R.id.importe);
            fondo = itemView.findViewById(R.id.fondo);
            laySel = itemView.findViewById(R.id.laySel);
            layDatos = itemView.findViewById(R.id.layDatos);
            etPax = itemView.findViewById(R.id.etPax);
            etRoom = itemView.findViewById(R.id.etRoom);

            ivAceptar = itemView.findViewById(R.id.ivAceptar);
            ivCancelar = itemView.findViewById(R.id.ivCancelar);
            ivEstado = itemView.findViewById(R.id.ivEstado);
            ivMesa = itemView.findViewById(R.id.ivMesa);
            ivPax = itemView.findViewById(R.id.ivPax);
            ivRoom = itemView.findViewById(R.id.ivRoom);
            ivExterno = itemView.findViewById(R.id.ivExterno);

            etPax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (v.getId() == R.id.etPax && hasFocus && v.getVisibility() == VISIBLE){
                       Handler h = new Handler();
                       h.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               mostrarTeclado( activity, etPax,"2");
                           }
                       }, 300);
                    }

                }
            });

            etRoom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (v.getId() == R.id.etRoom && hasFocus && v.getVisibility() == VISIBLE){
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mostrarTeclado( activity, etRoom, "");
                            }
                        }, 300);
                    }


                }
            });

        }

        private static void mostrarTeclado(Activity activity, EditText editText, String value){
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                focusView = editText;
                focusActivity = activity;
                if (editText.getText().toString().trim().equals("")) {
                    editText.setText(value);
                    editText.selectAll();
                }
        }



    }

    DialogInterface.OnClickListener onLongClickPidePax =  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {

                int pax = 0;
                for (ClaseSubMesas submesa : listaMesasFiltradas.get(mesaSel).submesas) {
                    pax += submesa.pax;
                }
                listaMesasFiltradas.get(mesaSel).pax = pax; // totaliza pax en la mesa

                interfaceMesas.actualizaPax(listaMesasFiltradas.get(mesaSel).submesas, listaMesasFiltradas.get(mesaSel).mesa);

            }
            else {
                int pax = 0;
                for (ClaseSubMesas submesa: listaMesasFiltradas.get(mesaSel).submesas){
                    submesa.pax += submesa.paxTraspaso;
                    submesa.paxTraspaso = 0;
                    pax += submesa.pax;
                }
                listaMesasFiltradas.get(mesaSel).pax = pax; // totaliza pax en la mesa
            }
        }
    };

}