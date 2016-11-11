package mx.edu.itmorelia.pdm.cajero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class InicioActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scanner;
    private String inputText;

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

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void handleResult(final Result result){

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
                Toast.makeText(getApplicationContext(), inputText, Toast.LENGTH_SHORT).show();
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




}
