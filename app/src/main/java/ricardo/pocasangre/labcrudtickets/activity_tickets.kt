package ricardo.pocasangre.labcrudtickets

import Modelo.Conexion
import Modelo.DataClassTickets
import ReciclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class activity_tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNumTicket = findViewById<EditText>(R.id.txtNumTicket)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val txtFechaInicio = findViewById<EditText>(R.id.txtFechaInicio)
        val txtFechaFinal = findViewById<EditText>(R.id.txtFechaFinal)
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtEmailAutor = findViewById<EditText>(R.id.txtEmailAutor)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<DataClassTickets>{

            val objConexion = Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Tickets")!!

            val ListaTicket = mutableListOf<DataClassTickets>()

            while (resultSet.next()){

                val UUID_TICKET = resultSet.getString("UUID_TICKET")
                val num_ticket = resultSet.getInt("num_ticket")
                val titulo = resultSet.getString("titulo")
                val descripcion = resultSet.getString("descripcion")
                val autor = resultSet.getString("autor")
                val email_autor = resultSet.getString("email_autor")
                val fecha_ticket = resultSet.getString("fecha_ticket")
                val estado = resultSet.getString("estado")
                val fecha_fin_ticket = resultSet.getString("fecha_fin_ticket")

                val valoresJuntos = DataClassTickets(UUID_TICKET, num_ticket, titulo, descripcion, autor, email_autor, fecha_ticket, estado, fecha_fin_ticket)

                ListaTicket.add(valoresJuntos)
            }
            return ListaTicket

        }

        CoroutineScope(Dispatchers.IO).launch {
            val TicketDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(TicketDB)
                rcvTicket.adapter = adapter
            }
        }

        val btnTicket = findViewById<Button>(R.id.btnTicket)

        btnTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = Conexion().cadenaConexion()

                try {


                    // Validación de campos vacíos
                    if (txtNumTicket.text.isEmpty() || txtTitulo.text.isEmpty() || txtDescripcion.text.isEmpty() ||
                        txtAutor.text.isEmpty() || txtEmailAutor.text.isEmpty() || txtFechaInicio.text.isEmpty() ||
                        txtFechaFinal.text.isEmpty()
                    ) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@activity_tickets,
                                "Por favor, completa todos los campos.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@launch
                    }


                    val addTicket =
                        objConexion?.prepareStatement("insert into Ticket (UUID_TICKET, num_ticket, titulo, descripcion, autor, email_autor, fecha_ticket, estado, fecha_fin_ticket) values (?,?,?,?,?,?,?,?,?)")!!

                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setInt(2, txtNumTicket.text.toString().toInt())
                    addTicket.setString(3, txtTitulo.text.toString())
                    addTicket.setString(4, txtDescripcion.text.toString())
                    addTicket.setString(5, txtAutor.text.toString())
                    addTicket.setString(6, txtEmailAutor.text.toString())
                    addTicket.setString(7, txtEstado.text.toString())
                    addTicket.setString(8, txtFechaInicio.text.toString())
                    addTicket.setString(9, txtFechaFinal.text.toString())

                    addTicket.executeUpdate()

                    //Refresco la lista
                    val nuevasMascotas = obtenerTickets()
                    withContext(Dispatchers.Main) {
                        (rcvTicket.adapter as? Adaptador)?.actualizarLista(nuevasMascotas)
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_tickets, "Ticket creado", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@activity_tickets, "Error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        println("Error: ${e.message}")
                        e.printStackTrace()
                    }

                }
            }


        }
    }
}