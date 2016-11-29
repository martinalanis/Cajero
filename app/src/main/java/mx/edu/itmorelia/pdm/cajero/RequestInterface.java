package mx.edu.itmorelia.pdm.cajero;

import mx.edu.itmorelia.pdm.cajero.models.ServerRequest;
import mx.edu.itmorelia.pdm.cajero.models.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Martin Alanis on 25/11/2016.
 */

public interface RequestInterface {
    @POST("pdmcajero/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
