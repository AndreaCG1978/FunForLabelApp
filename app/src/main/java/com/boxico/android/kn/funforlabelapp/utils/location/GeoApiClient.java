package com.boxico.android.kn.funforlabelapp.utils.location;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoApiClient {

    private static Retrofit sRetrofit = null;

    public GeoApiClient() {
    }

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            synchronized (Retrofit.class) {
                if (sRetrofit == null) {
                    GsonBuilder gsonB = new GsonBuilder();
                    gsonB.setLenient();
                    Gson gson = gsonB.create();
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.level(HttpLoggingInterceptor.Level.BODY);

                    Interceptor interceptor2 = new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException
                        {
                            okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                            ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                            ongoing.addHeader("Accept", "application/json");

                            return chain.proceed(ongoing.build());
                        }
                    };

                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(interceptor2).connectTimeout(100, TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS).build();
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(ConstantsAdmin.GEOAPIURL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                 //   customerService = retrofit.create(CustomerService.class);

                    /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(ConstantsAdmin.GEOAPIURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();*/
                }
            }
        }
        return sRetrofit;
    }

}