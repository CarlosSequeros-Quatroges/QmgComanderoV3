package es.quatroges.qgestpv_v3.webservice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static int connectionTimeOut;
    private static int readTimeOut;
    private static int writeTimeOut;



    public static Retrofit getClient(String baseUrl, boolean force,int  connectionTO,int  readTO,int writeTO, Long latencia) {
        if (latencia > 500){
            connectionTO = 2*connectionTO;
            writeTO =2*writeTO;
            readTO = 2*readTO;
        }

        if (force){
            retrofit = null;
        }
        else {
            if (RetrofitClient.connectionTimeOut != connectionTO ||
                    RetrofitClient.readTimeOut != readTO ||
                    RetrofitClient.writeTimeOut != writeTO ){
                retrofit = null;
                RetrofitClient.connectionTimeOut = connectionTO;
                RetrofitClient.readTimeOut = readTO;
                RetrofitClient.writeTimeOut = writeTO;
            }
        }


        if (retrofit==null ) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(connectionTO, TimeUnit.SECONDS)
                    .readTimeout(readTO,TimeUnit.SECONDS)
                    .writeTimeout(writeTO,TimeUnit.SECONDS);

            httpClient.addInterceptor(logging);  // <-- this is the important line!

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
