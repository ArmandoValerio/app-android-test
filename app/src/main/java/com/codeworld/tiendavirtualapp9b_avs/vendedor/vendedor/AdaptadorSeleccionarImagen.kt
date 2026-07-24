package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codeworld.tiendavirtualapp9b_avs.R
import com.codeworld.tiendavirtualapp9b_avs.databinding.ItemImagenSeleccionarBinding

class AdaptadorSeleccionarImagen(
    private val listaUris: ArrayList<Uri?>,
    private val onAgregarClick: (Int) -> Unit,
    private val onBorrarClick: (Int) -> Unit
) : RecyclerView.Adapter<AdaptadorSeleccionarImagen.ViewHolder>() {

    class ViewHolder(val binding: ItemImagenSeleccionarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImagenSeleccionarBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = listaUris[position]

        if (uri != null) {
            // Si tiene foto, la mostramos y habilitamos la opción de borrar
            holder.binding.imgSeleccionada.setImageURI(uri)
            holder.binding.btnBorrar.visibility = View.VISIBLE
            holder.binding.imgSeleccionada.setOnClickListener(null)
        } else {
            // Si no tiene foto, mostramos el placeholder de agregar
            holder.binding.imgSeleccionada.setImageResource(R.drawable.item_imagen)
            holder.binding.btnBorrar.visibility = View.GONE
            holder.binding.imgSeleccionada.setOnClickListener {
                onAgregarClick(position)
            }
        }

        holder.binding.btnBorrar.setOnClickListener {
            onBorrarClick(position)
        }
    }

    override fun getItemCount(): Int = listaUris.size
}