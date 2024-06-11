package ReciclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ricardo.pocasangre.labcrudtickets.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)

}