package mx.edu.itmorelia.pdm.cajero;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        Bundle b = new Bundle();
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Ingrese sus datos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void handleResult(Result result){

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
                if(inputText.equals("1234")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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


}
