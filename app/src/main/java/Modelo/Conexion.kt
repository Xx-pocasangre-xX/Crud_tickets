package Modelo

import java.sql.Connection
import java.sql.DriverManager

class Conexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.0.10:1521:xe"
            val usuario ="Ricardo_developer"
            val contrasena = "Ricardo2024."

            val conexion = DriverManager.getConnection(url, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("aqui esta el error: $e")
            return null
        }
    }
}