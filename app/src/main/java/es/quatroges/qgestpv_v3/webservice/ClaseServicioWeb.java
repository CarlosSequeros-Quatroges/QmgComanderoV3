package es.quatroges.qgestpv_v3.webservice;

import java.io.Serializable;


public class ClaseServicioWeb {
   public static String BASE_URL = "http://ws.gesoftasociados.com:60880/";

    public   ClaseServicioWeb(String url) {
        BASE_URL = url;
    }

    public static APIService getAPIService(boolean force, int conectionTimeout, int readTimeout, int writeTimeout, Long latenciaTest) {
        return RetrofitClient.getClient(BASE_URL,force,conectionTimeout,readTimeout,writeTimeout,latenciaTest).create(APIService.class);

        /*
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(conectionTimeout, TimeUnit.MINUTES)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(APIService.class);
*/
    }


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    private class valorespordefecto implements Serializable {

    }

}

