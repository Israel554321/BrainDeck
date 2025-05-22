package com.ised.catarsisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {
    EditText NombreEt, CorreoEt, ContraseñaEt, ConfirmarContraseñaEt;
    Button Registrarusuario;
    TextView TengounacuentaTXT;
FirebaseAuth firebaseAuth;
ProgressDialog progressDialog;


String nombre="", correo="", contraseña="", confirmarcontraseña="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Registrar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        NombreEt= findViewById(R.id.NombreEt);
        CorreoEt= findViewById(R.id.CorreoEt);
        ContraseñaEt= findViewById(R.id.ContraseñaEt);
        ConfirmarContraseñaEt= findViewById(R.id.ConfirmarContraseñaEt);
        Registrarusuario= findViewById(R.id.Registrarusuario);
        TengounacuentaTXT= findViewById(R.id.TengounacuentaTXT);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(Registro.this);
        progressDialog.setMessage("Espere un poco :3");
//        para que cierre hasta que finalice sin interrupciones
        progressDialog.setCanceledOnTouchOutside(false);

//        este es un evento
        Registrarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                    ValidarDatos();

            }
        });


        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //
                startActivity(new Intent (Registro.this,Login.class));
            }
        });






        }
        private void ValidarDatos(){

        nombre= NombreEt.getText().toString();
        correo= CorreoEt.getText().toString();
        contraseña= ContraseñaEt.getText().toString();
        confirmarcontraseña= ConfirmarContraseñaEt.getText().toString();

            //        esto es para cuanco no se llena el campo
            if (TextUtils.isEmpty(nombre)){
                Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();
            }

            else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            }

            else if (TextUtils.isEmpty(contraseña)){
                Toast.makeText(this,"Ingrese contraseña", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(confirmarcontraseña)){
                Toast.makeText(this,"Confirme su contraseña", Toast.LENGTH_SHORT).show();
            }
            else if (!contraseña.equals(confirmarcontraseña)){
                Toast.makeText(this,"Las contraseñas no son las mismas unu", Toast.LENGTH_SHORT).show();

            }

            else {
                CrearCuenta();
            }






        }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();


//        aqui se crea un usuario en firebase
        firebaseAuth.createUserWithEmailAndPassword(correo,contraseña)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        GuardarInformacion();

                    }
                } ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        progressDialog.dismiss();
                    Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                });
    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Espere un poco, estamos guardando su informacion ^-^");
        progressDialog.dismiss();
//        se obtiene la identificacion del usuario actual
        String vid = firebaseAuth.getUid();
//se asignan claves unicas para los valores
        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", vid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("contraseña", contraseña);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(vid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "La cuenta se creo con exito ^-^", Toast.LENGTH_SHORT).show();
                        Log.d("Registro", "Datos guardados para UID: " + vid); // Agregar esto
                        startActivity(new Intent(Registro.this, MenuPrincipal.class));
                        finish();

                    }



    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(Registro.this," "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

    }
    public boolean onSupportNavigateUp(){

        onBackPressed();
        return super.onSupportNavigateUp();
    }




}

