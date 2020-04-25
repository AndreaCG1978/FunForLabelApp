package com.boxico.android.kn.funforlabelapp.utils.location;

import android.content.ContentValues;
import android.os.AsyncTask;


import com.boxico.android.kn.funforlabelapp.services.GeoService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class LocationManager {

    private static List<Geoname> provincias;
    private static List<Geoname> ciudades;
    private static String geoIdProvincia = "0";

    public static List<Geoname> getProvincias() {
        return provincias;
    }

    public static void setProvincias(List<Geoname> provincias) {
        LocationManager.provincias = provincias;
    }

    public static List<Geoname> getCiudades() {
        return ciudades;
    }

    public static void setCiudades(List<Geoname> ciudades) {
        LocationManager.ciudades = ciudades;
    }

    public static String getGeoIdProvincia() {
        return geoIdProvincia;
    }

    public static void setGeoIdProvincia(String geoIdProvincia) {
        LocationManager.geoIdProvincia = geoIdProvincia;
    }


    public static void initialize(){
        //new GetProvinciasTask().execute();

        try {
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<Paises> responseCallPais = service.getPaises(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, ConstantsAdmin.GEOCODIGOARGENTINA);
            Paises paises = responseCallPais.execute().body();
            Call<GeoChilds> responseCallProvincias =
                    service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, String.valueOf(paises.getPaises().get(0).getGeonameId()));
            GeoChilds pcias = responseCallProvincias.execute().body();
            if (pcias != null) {
                provincias = pcias.getChilds();
            /*    Geoname child = pcias.getChilds().get(0);
                Call<GeoChilds> responseCallCiudades =
                            service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, String.valueOf(child.getGeonameId()));
                    //inserted = getContentResolver().bulkInsert(CountriesEntry.CONTENT_URI, contentValues);
                GeoChilds cdades = responseCallCiudades.execute().body();
                  if(cdades != null){
                     ciudades = cdades.getChilds();
                  }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void recargarCiudades(){
        //new GetProvinciasTask().execute();

        try {
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<GeoChilds> responseCallCiudades =
                       service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, getGeoIdProvincia());
                //inserted = getContentResolver().bulkInsert(CountriesEntry.CONTENT_URI, contentValues);
            GeoChilds cdades = responseCallCiudades.execute().body();
            if(cdades != null){
                ciudades = cdades.getChilds();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static ContentValues childsToContentValues(Geoname child) {
        ContentValues cv = new ContentValues();
        cv.put("Nombre",  child.name);
        cv.put("Id", child.geonameId);
        return cv;
    }

/*

    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();
                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("username", ConstantsAdmin.GEOUSERNAME)
                        .build();      // Request customization: add request headers
                final Request.Builder requestBuilder = original.newBuilder().url(url);
                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ConstantsAdmin.GEOAPIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
    }
*/

    /*
    private static void initializeService(){

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsAdmin.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        geolocationService = retrofit.create(GeoService.class);
    }
*/
/*
    public static void cargarProvincias(Context ctx){
        new Gson().fromJson(getProvincias(ctx), new TypeToken<Map<String, List<String>>>() { }.getType());
    }
*/
  /*  private static void getProvincias(Context ctx) {
       BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(ctx.getAssets().open("paisProvinciaLocalidad.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // do reading, usually loop until end of file reading
        String mLine = null;
        try {
            mLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mLine;
    }
*/
}
