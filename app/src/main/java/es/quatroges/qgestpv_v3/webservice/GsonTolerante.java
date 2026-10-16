package es.quatroges.qgestpv_v3.webservice;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gson que tolera valores numericos mal formados en las respuestas de la API.
 *
 * El servidor envia a veces cadenas vacias ("pos":"") o null en campos declarados como int, long, double o float
 * en los modelos. Gson por defecto lanza JsonSyntaxException (NumberFormatException: empty String) y aborta la
 * sincronizacion de toda la tabla. Con estos adaptadores el valor se interpreta como 0 y se deja traza en el log.
 */
public final class GsonTolerante {

    private static final String TAG = "GsonTolerante";
    private static Gson instancia;

    private GsonTolerante() {
    }

    public static synchronized Gson crear() {
        if (instancia == null) {
            instancia = new GsonBuilder()
                    .registerTypeAdapter(int.class, new AdaptadorNumerico<>(Integer.class, 0))
                    .registerTypeAdapter(Integer.class, new AdaptadorNumerico<>(Integer.class, 0))
                    .registerTypeAdapter(long.class, new AdaptadorNumerico<>(Long.class, 0L))
                    .registerTypeAdapter(Long.class, new AdaptadorNumerico<>(Long.class, 0L))
                    .registerTypeAdapter(double.class, new AdaptadorNumerico<>(Double.class, 0.0))
                    .registerTypeAdapter(Double.class, new AdaptadorNumerico<>(Double.class, 0.0))
                    .registerTypeAdapter(float.class, new AdaptadorNumerico<>(Float.class, 0.0f))
                    .registerTypeAdapter(Float.class, new AdaptadorNumerico<>(Float.class, 0.0f))
                    .create();
        }
        return instancia;
    }

    /** Lee un numero aceptando null, cadena vacia y cadenas con coma decimal; ante un valor no numerico devuelve el valor por defecto. */
    private static final class AdaptadorNumerico<T extends Number> extends TypeAdapter<T> {
        private final Class<T> tipo;
        private final T porDefecto;

        AdaptadorNumerico(Class<T> tipo, T porDefecto) {
            this.tipo = tipo;
            this.porDefecto = porDefecto;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            if (token == JsonToken.NULL) {
                in.nextNull();
                return porDefecto;
            }
            String texto = token == JsonToken.BOOLEAN ? (in.nextBoolean() ? "1" : "0") : in.nextString();
            String limpio = texto == null ? "" : texto.trim().replace(',', '.');
            if (limpio.isEmpty()) {
                return porDefecto;
            }
            try {
                return convertir(limpio);
            } catch (NumberFormatException e) {
                Log.w(TAG, "Valor numerico no valido '" + texto + "' para " + tipo.getSimpleName() + ": se usa " + porDefecto);
                return porDefecto;
            }
        }

        @SuppressWarnings("unchecked")
        private T convertir(String s) {
            if (tipo == Integer.class) {
                // admite "15.0" que algunos servidores envian para enteros
                return (T) Integer.valueOf((int) Double.parseDouble(s));
            }
            if (tipo == Long.class) {
                return (T) Long.valueOf((long) Double.parseDouble(s));
            }
            if (tipo == Float.class) {
                return (T) Float.valueOf(s);
            }
            return (T) Double.valueOf(s);
        }
    }
}
