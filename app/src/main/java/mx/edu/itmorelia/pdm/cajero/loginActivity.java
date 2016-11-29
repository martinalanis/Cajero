package mx.edu.itmorelia.pdm.cajero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import mx.edu.itmorelia.pdm.cajero.models.ServerRequest;
import mx.edu.itmorelia.pdm.cajero.models.ServerResponse;
import mx.edu.itmorelia.pdm.cajero.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class loginActivity extends AppCompatActivity {

    private EditText cuenta, pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        cuenta = (EditText) findViewById(R.id.login_cuenta);
        pin = (EditText) findViewById(R.id.login_pin);
    }

    public void login (View view) {
        String cta = cuenta.getText().toString();
        String pass = pin.getText().toString();

        if(!cta.isEmpty() && !pass.isEmpty()){
            loginProcess(cta, pass);
        }else{
            Snackbar.make(view, "Los campos estan vacios", Snackbar.LENGTH_LONG).show();
        }

    }

    private void loginProcess(String cuenta, String pin) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setCuenta(cuenta);
        user.setPin(pin);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getApplicationContext(), resp.getMessage(), Toast.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(Constants.NOMBRE, resp.getUser().getNombre());
                    intent.putExtra(Constants.PATERNO, resp.getUser().getPaterno());
                    intent.putExtra(Constants.MATERNO, resp.getUser().getMaterno());
                    intent.putExtra(Constants.CUENTA, resp.getUser().getCuenta());
                    intent.putExtra(Constants.ID, resp.getUser().getId());
                    /*Cerrar activities previas*/
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
