package Modelo

data class DataClassTickets (

    val UUID_Ticket: String,
    val num_Ticket: Int,
    var titulo: String,
    val descripcion: String,
    val autor:String,
    val email_autor: String,
    val fecha_ticket: String,
    val estado: String,
    val fecha_fin_ticket: String

)