package mx.edu.itmorelia.pdm.cajero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        if(cuenta.getText().toString().equals("1234") && pin.getText().toString().equals("1234")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Datos erroneos", Toast.LENGTH_LONG).show();
        }
    }
}
