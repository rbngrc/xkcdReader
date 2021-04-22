package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

public class MainActivity extends Activity {
    private EditText et1, et2, et3, et4;

    private Executor bdExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // proviene del layout, son los campos de texto
        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText4);

        bdExecutor = ((MyApplication) getApplication()).diskIOExecutor;
    }

    // Damos de alta los usuarios en nuestra aplicación
    public void alta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et1.getText().toString();
        String nombre = et2.getText().toString();
        String ciudad = et3.getText().toString();
        String numero = et4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("dni", dni);
        registro.put("nombre", nombre);
        registro.put("ciudad", ciudad);
        registro.put("numero", numero);
        // los inserto en la base de datos
        bd.insert("usuario", null, registro);
        bd.close();
        // ponemos los campos a vacío para insertar el siguiente usuario
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        Toast.makeText(this, "Datos del usuario cargados", Toast.LENGTH_SHORT).show();
    }

    // Hacemos búsqueda de usuario por DNI
    public void consulta(View v) {
        String dni = et1.getText().toString();
        Context context = this;
        bdExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor fila = bd.rawQuery("select nombre, ciudad, numero from usuario where dni=" + dni, null);
                if (fila.moveToFirst()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et2.setText(fila.getString(0));
                            et3.setText(fila.getString(1));
                            et4.setText(fila.getString(2));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "No existe ningún usuario con ese dni", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                bd.close();
            }
        });
    }

    /* Método para dar de baja al usuario insertado*/
    public void baja(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et1.getText().toString();
        // aquí borro la base de datos del usuario por el dni
        int cant = bd.delete("usuario", "dni=" + dni, null);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        if (cant == 1)
            Toast.makeText(this, "Usuario eliminado",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe usuario",
                    Toast.LENGTH_SHORT).show();
    }

    // Método para modificar la información del usuario
    public void modificacion(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String dni = et1.getText().toString();
        String nombre = et2.getText().toString();
        String ciudad = et3.getText().toString();
        String numero = et4.getText().toString();
        ContentValues registro = new ContentValues();
        // actualizamos con los nuevos datos, la información cambiada
        registro.put("nombre", nombre);
        registro.put("ciudad", ciudad);
        registro.put("numero", numero);
        int cant = bd.update("usuario", registro, "dni=" + dni, null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this, "Datos modificados con éxito", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe usuario", Toast.LENGTH_SHORT).show();
    }
}