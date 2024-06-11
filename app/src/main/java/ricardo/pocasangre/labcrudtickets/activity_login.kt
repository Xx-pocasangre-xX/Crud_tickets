package ricardo.pocasangre.labcrudtickets

import Modelo.Conexion
import Modelo.DataClassTickets
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreoLogin = findViewById<EditText>(R.id.txtCorreoLogin)
        val txtContrasenaLogin = findViewById<EditText>(R.id.txtContrasenaLogin)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)

        btnIniciarSesion.setOnClickListener {
            val ticketsIntent = Intent(this, DataClassTickets::class.java)


            //Evitamos que la aplicacion se cierre si no llenamos algun campo

            //Utilice el lifecycleScope en vez de GlobalScope Para asegurar que las corutinas se cancelen automáticamente cuando la actividad se termine.
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val objConexion = Conexion().cadenaConexion()
                    val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM Usuarios WHERE nom_usuario = ? AND contrasena = ?")

                    comprobarUsuario?.apply {
                        setString(1, txtCorreoLogin.text.toString())
                        setString(2, txtContrasenaLogin.text.toString())

                        val resultado = executeQuery()
                        if (resultado.next()) {
                            withContext(Dispatchers.Main) {
                                startActivity(ticketsIntent)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@activity_login, "Usuario no encontrado, verifique las credenciales", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@activity_login, "Error de conexión.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_login, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}