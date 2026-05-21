package es.quatroges.qgestpv_v3;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.adapters.RvAdapterNotas;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class FragmentDialogNotas extends Fragment {

    private static ArrayList<ClaseItemExtra> notas;
    private static RvAdapterNotas adapterNotas;

    private EditText etNota;
    private NestedScrollView nsvNotas;
    private View layAccionesNota;
    private int posSel = -1;

    public static ArrayList<ClaseItemExtra> getNotas() {
        return notas;
    }

    private static ImageView ivAnadir,ivAceptar,ivCancelar,ivBorrar;


    public interface InterfaceFrgDlgNotas {
        ArrayList<ClaseItemExtra> ntCargaDatosNotas();

    }

    InterfaceFrgDlgNotas interfaceFrgDlgNotas;

    public FragmentDialogNotas() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof InterfaceFrgDlgNotas) {
            interfaceFrgDlgNotas = (InterfaceFrgDlgNotas) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_notas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notas = interfaceFrgDlgNotas.ntCargaDatosNotas();
        if (notas == null) {
            notas = new ArrayList<>();
        }

        etNota = view.findViewById(R.id.etNota);
        nsvNotas = view.findViewById(R.id.nsvNotas);
        layAccionesNota = view.findViewById(R.id.layAccionesNota);
        etNota.setEnabled(false);
        etNota.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mostrarTeclado(etNota);
                asegurarVisibilidadEditor();
            } else {
                ocultarTeclado(etNota);
            }
        });
        

        RecyclerView rvNotas = view.findViewById(R.id.rvNotas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rvNotas.setLayoutManager(gridLayoutManager);

        adapterNotas = new RvAdapterNotas(getContext(), notas, new RvAdapterNotas.InterfaceOnClickNota() {
            @Override
            public void onClickNota(int position, ClaseItemExtra nota) {
                posSel = position;
                adapterNotas.setPosicionSeleccionada(posSel);
                etNota.setText(nota != null ? nota.nota : "");
                etNota.setSelection(etNota.getText().length());

                ivAnadir.setVisibility(INVISIBLE);
                ivAceptar.setVisibility(VISIBLE);
                ivCancelar.setVisibility(VISIBLE);
                ivBorrar.setVisibility(VISIBLE);

                etNota.setEnabled(true);
                focusYMostrarTeclado();


            }
        });
        rvNotas.setAdapter(adapterNotas);

        ivAnadir = view.findViewById(R.id.ivAnadirNota);
        ivAceptar = view.findViewById(R.id.ivAceptarNota);
        ivCancelar = view.findViewById(R.id.ivCancelarNota);
        ivBorrar = view.findViewById(R.id.ivBorrarNota);

        ivAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posSel = -1;
                adapterNotas.setPosicionSeleccionada(-1);
                etNota.setText("");
                etNota.requestFocus();

                ivAnadir.setVisibility(INVISIBLE);
                ivAceptar.setVisibility(VISIBLE);
                ivCancelar.setVisibility(VISIBLE);
                ivBorrar.setVisibility(INVISIBLE);

                etNota.setEnabled(true);
                focusYMostrarTeclado();


            }
        });

        ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAnadir.setVisibility(VISIBLE);
                ivAceptar.setVisibility(INVISIBLE);
                ivCancelar.setVisibility(INVISIBLE);
                ivBorrar.setVisibility(INVISIBLE);
                etNota.setEnabled(false);


                String texto = etNota.getText().toString().trim();
                if (texto.length() == 0) {
                    return;
                }

                if (posSel >= 0 && posSel < notas.size()) {
                    notas.get(posSel).nota = texto;
                    notas.get(posSel).estado = ClaseUtils.enEstado.actualizar;
                } else {
                    notas.add(new ClaseItemExtra(0, "N",texto, ClaseUtils.now("HH:mm:ss"), ClaseUtils.enEstado.anadir));
                    rvNotas.scrollToPosition(notas.size() - 1);
                }
                limpiarEdicion();
                adapterNotas.notifyDataSetChanged();


            }
        });

        ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarEdicion();

                ivAnadir.setVisibility(VISIBLE);
                ivAceptar.setVisibility(INVISIBLE);
                ivCancelar.setVisibility(INVISIBLE);
                ivBorrar.setVisibility(INVISIBLE);
                etNota.setEnabled(false);
            }
        });

        ivBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivAnadir.setVisibility(VISIBLE);
                ivAceptar.setVisibility(INVISIBLE);
                ivCancelar.setVisibility(INVISIBLE);
                ivBorrar.setVisibility(INVISIBLE);
                etNota.setEnabled(false);

                if (posSel >= 0 && posSel < notas.size()) {

                    if (notas.get(posSel).codigo == 0 ) {
                        notas.remove(posSel);
                    }
                    else {
                        notas.get(posSel).estado = ClaseUtils.enEstado.eliminar;
                    }
                    limpiarEdicion();
                    adapterNotas.notifyDataSetChanged();
                    return;
                }

                if (etNota.getText().toString().trim().length() > 0) {
                    etNota.setText("");
                    return;
                }

                notas.clear();
                limpiarEdicion();
                adapterNotas.notifyDataSetChanged();


            }
        });

        ivAnadir.setVisibility(VISIBLE);
        ivAceptar.setVisibility(INVISIBLE);
        ivCancelar.setVisibility(INVISIBLE);
        ivBorrar.setVisibility(INVISIBLE);


    }

    private void limpiarEdicion() {
        posSel = -1;
        if (adapterNotas != null) {
            adapterNotas.setPosicionSeleccionada(-1);
        }
        if (etNota != null) {
            etNota.setText("");
            etNota.clearFocus();
        }
    }

    private void focusYMostrarTeclado() {
        if (etNota == null) {
            return;
        }
        etNota.requestFocus();
        etNota.post(() -> {
            mostrarTeclado(etNota);
            asegurarVisibilidadEditor();
        });
    }

    private void mostrarTeclado(View v) {
        if (v == null || getContext() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void ocultarTeclado(View v) {
        if (v == null || getContext() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void asegurarVisibilidadEditor() {
        if (nsvNotas == null || etNota == null) {
            return;
        }

        nsvNotas.post(() -> {
            int yObjetivo = etNota.getTop();
            if (layAccionesNota != null) {
                int yAcciones = layAccionesNota.getBottom() - nsvNotas.getHeight();
                yObjetivo = Math.max(yObjetivo, yAcciones);
            }
            nsvNotas.smoothScrollTo(0, Math.max(0, yObjetivo));
        });
    }

    public static FragmentDialogNotas newInstance(String text) {
        FragmentDialogNotas f = new FragmentDialogNotas();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }



}
