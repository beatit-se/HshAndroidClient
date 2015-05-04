package se.beatit.homesweethome.io.restclient;

import retrofit.RestAdapter;

/**
 * Created by stefan on 2015-02-18.
 */
public class RetrofitRestClient {
    public static IotHomeRestRoot getIoTHomeServerRestClient(String serverBaseUri) {
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(serverBaseUri)
                .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

        return restAdapter.create(IotHomeRestRoot.class);
    }
}
