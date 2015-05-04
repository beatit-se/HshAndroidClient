package se.beatit.homesweethome.io.restclient;


import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by stefan on 2015-02-18.
 */
public interface IotHomeRestRoot {

    @GET("/iothome/{home}/electricityuse/{from}/{to}/{resolution}")
    public History gethistoricUsage(@Path("home") String home, @Path("from") String from, @Path("to") String to, @Path("resolution") String resolution);
}
