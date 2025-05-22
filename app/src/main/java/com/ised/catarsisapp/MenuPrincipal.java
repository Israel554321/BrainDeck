package com.ised.catarsisapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ised.catarsisapp.CrearMazo.Crear_Mazo;
import com.ised.catarsisapp.EditarMazo.Editar_Mazo;
import com.ised.catarsisapp.ModoJuego.Modo_Juego;
import com.ised.catarsisapp.Puntajes.Puntajes;
import com.ised.catarsisapp.VerMazo.Ver_Mazo;

public class MenuPrincipal extends AppCompatActivity {
    Button CerrarSesion,VerMazo,CrearMazo,ModoJuego,Puntajes,EditarMazo;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView UidPrincipal,NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBarDatos;

    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("BrainDeck");

        VerMazo = findViewById(R.id.VerMazo);
        CrearMazo = findViewById(R.id.CrearMazo);
        ModoJuego = findViewById(R.id.ModoJuego);
        Puntajes = findViewById(R.id.Puntajes);
        EditarMazo = findViewById(R.id.EditarMazo);
        UidPrincipal = findViewById(R.id.UidPrincipal);
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);
//la base de datos que se va a usar es la de usuarios en Firebase
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        CerrarSesion = findViewById(R.id.CerrarSesion);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
//creacion de eventos para cada boton del menu principal osea las opciones


        CrearMazo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Crear_Mazo.class));

                //verificar si el cambio de actividad se hace correctamente
                Toast.makeText(MenuPrincipal.this, "Creando Mazo", Toast.LENGTH_SHORT).show();
            }
        });

        EditarMazo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Editar_Mazo.class));

                //verificar si el cambio de actividad se hace correctamente
                Toast.makeText(MenuPrincipal.this, "Editando Mazo", Toast.LENGTH_SHORT).show();

            }
        });

        ModoJuego.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Modo_Juego.class));

                //verificar si el cambio de actividad se hace correctamente
                Toast.makeText(MenuPrincipal.this, "Modo de Juego", Toast.LENGTH_SHORT).show();

            }
        });

        Puntajes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Puntajes.class));

                //verificar si el cambio de actividad se hace correctamente
                Toast.makeText(MenuPrincipal.this, "Puntajes", Toast.LENGTH_SHORT).show();

            }
        });

        VerMazo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Ver_Mazo.class));

                //verificar si el cambio de actividad se hace correctamente
                Toast.makeText(MenuPrincipal.this, "Ver Mazo", Toast.LENGTH_SHORT).show();

            }
        });







//        el evento para cuando se presione el boton cerrar sesion
        CerrarSesion.setOnClickListener( new View.OnClickListener(){
           @Override
            public void onClick(View view) {
               SalirAplicacion();
           }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();

    }

    private void ComprobarInicioSesion() {

        if(user != null){
            //si el usuario ha iniciado sesión
            CargaDeDatos();
        }else{
            //si no que mande del menu principal a el main activity y finaliza
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }
    private void CargaDeDatos(){
        //user contiene el usuario que ha iniciado sesion actualmente
        Log.d("MenuPrincipal", "Intentando cargar datos para UID: " + user.getUid()); // Agregar esto
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
// permitira leer los datos en tiempo real que contiene usuarios en database
                //si el usuario existe....
                if(snapshot.exists()){
                    //el progressBar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    //luego se muestran nombres principal y cuenta (Texts view)
                    UidPrincipal.setVisibility(View.VISIBLE);
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    //Obtener los datos de la base de datos
                    String uid = " "+ snapshot.child("uid").getValue();
                    String nombre = " "+ snapshot.child("nombre").getValue();
                    String correo = " "+ snapshot.child("correo").getValue();

                    //mostrar los datos en los texts view
                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);

                    //habilitar los botones del menu

                    VerMazo.setEnabled(true);
                    CrearMazo.setEnabled(true);
                    ModoJuego.setEnabled(true);
                    Puntajes.setEnabled(true);
                    EditarMazo.setEnabled(true);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        finish();
        Toast.makeText(this, "se cerro la sesión, vuelva pronto ^-^", Toast.LENGTH_SHORT).show();
    }
}