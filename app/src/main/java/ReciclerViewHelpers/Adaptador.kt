package ReciclerViewHelpers

import Modelo.Conexion
import Modelo.DataClassTickets
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ricardo.pocasangre.labcrudtickets.R
import ricardo.pocasangre.labcrudtickets.activity_detalle_ticket

class Adaptador(var Datos: List<DataClassTickets>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<DataClassTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualicePantalla(uuid: String, nuevoTitulo: String){
        val index = Datos.indexOfFirst { it.UUID_Ticket == uuid }
        Datos[index].titulo = nuevoTitulo
        notifyDataSetChanged()
    }

    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(titulo: String, posicion: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            //1- Creamos un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val deleteTicket = objConexion?.prepareStatement("delete from Tickets where titulo = ?")!!
            deleteTicket.setString(1, titulo)
            deleteTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarDato(nuevoTitulo: String, UUID_TICKET: String){
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de la clase de conexion
            val objConexion = Conexion().cadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val updateTicket = objConexion?.prepareStatement("update Tickets set titulo = ? where UUID_TICKET = ?")!!
            updateTicket.setString(1, nuevoTitulo)
            updateTicket.setString(2, UUID_TICKET)
            updateTicket.executeUpdate()

            withContext(Dispatchers.Main){
                actualicePantalla(UUID_TICKET, nuevoTitulo)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return ViewHolder(vista)

    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.txtNombreCard.text = item.titulo

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(item.titulo, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.imgEditar.setOnClickListener{
            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar el Ticket?")

            //Agregarle un cuadro de texto para
            //que el usuario escriba el nuevo nombre
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(item.titulo)
            builder.setView(cuadroTexto)

            //Botones
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarDato(cuadroTexto.text.toString(), item.UUID_Ticket)
            }

            builder.setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Todo: Clic a la card completa

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            //Cambiar de pantalla a la pantalla de detalle
            val pantallaDetalle = Intent(context, activity_detalle_ticket::class.java)
            //enviar a la otra pantalla todos mis valores
            pantallaDetalle.putExtra("UUID_TICKET", item.UUID_Ticket)
            pantallaDetalle.putExtra("num_ticket", item.num_Ticket)
            pantallaDetalle.putExtra("titulo", item.titulo)
            pantallaDetalle.putExtra("descripcion", item.descripcion)
            pantallaDetalle.putExtra("autor", item.autor)
            pantallaDetalle.putExtra("email_autor", item.email_autor)
            pantallaDetalle.putExtra("fecha_ticket", item.fecha_ticket)
            pantallaDetalle.putExtra("estado", item.estado)
            pantallaDetalle.putExtra("fecha_fin_ticket", item.fecha_fin_ticket)
            context.startActivity(pantallaDetalle)
        }

    }


}