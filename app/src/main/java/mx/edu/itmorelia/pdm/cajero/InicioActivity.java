package mx.edu.itmorelia.pdm.cajero;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import mx.edu.itmorelia.pdm.cajero.models.ServerRequest;
import mx.edu.itmorelia.pdm.cajero.models.ServerResponse;
import mx.edu.itmorelia.pdm.cajero.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InicioActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scanner;
    private String inputText;
    public static final int NOTIFICACION_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
    }

    public  void scan (View v){
        scanner = new ZXingScannerView(this);
        setContentView(scanner);
        scanner.setResultHandler(this);
        scanner.startCamera();
    }

    public void login_manual (View v){
        //Bundle b = new Bundle();
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        //finish();
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Ingrese sus datos", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }*/

    @Override
    public void handleResult(final Result result){

        final String res = result.getText().toString();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Bienvenido");
        alert.setMessage("Tu numero de cuenta es ****" + result.getText().substring(result.getText().length()-4));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        //AlertDialog alertDialog = alert.create();
        //alertDialog.show();
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputText = input.getText().toString();
                if(!inputText.isEmpty()){
                    loginProcess(res, inputText);
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    startNotification("Bienvenido "+ "Martin", "Haz iniciado sesión", "Sesión nueva");
                }else{
                    scanner.stopCamera();
                }
                //Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                //scanner.resumeCameraPreview(result);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle b = new Bundle();
                Intent i = new Intent(getApplicationContext(), InicioActivity.class);
                startActivity(i);
            }
        });

        alert.show();

        //scanner.resumeCameraPreview(this);
    }

    public void startNotification(String title, String text, String subtext){
        //Construccion de la accion del intent implicito
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://martinalanis.com"));
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);

        //Construccion de la notificacion;
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_check_circle_outline);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_circle_outline));
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSubText(subtext);

        //Enviar la notificacion
        NotificationManager notificationManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICACION_ID,builder.build());
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
