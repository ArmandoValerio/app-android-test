package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.nav_Fragments_Vendedor

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codeworld.tiendavirtualapp9b_avs.R
import com.codeworld.tiendavirtualapp9b_avs.databinding.FragmentAgregarProductoVBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FragmentAgregarProductoV : Fragment() {

    private var _binding: FragmentAgregarProductoVBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarProductoVBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnAgregarProducto.setOnClickListener {
            validarDatos()
        }
    }

    private var nombre = ""
    private var descripcion = ""
    private var precio = ""

    private fun validarDatos() {
        nombre = binding.etNombreProducto.text.toString().trim()
        descripcion = binding.etDescripcionProducto.text.toString().trim()
        precio = binding.etPrecioProducto.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombreProducto.error = "Ingrese el nombre del producto"
            binding.etNombreProducto.requestFocus()
        } else if (descripcion.isEmpty()) {
            binding.etDescripcionProducto.error = "Ingrese la descripción"
            binding.etDescripcionProducto.requestFocus()
        } else if (precio.isEmpty()) {
            binding.etPrecioProducto.error = "Ingrese el precio"
            binding.etPrecioProducto.requestFocus()
        } else {
            guardarProductoBD()
        }
    }

    private fun guardarProductoBD() {
        progressDialog.setMessage("Guardando producto en la BD...")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()
        val uidVendedor = firebaseAuth.uid!!

        val datosProducto = HashMap<String, Any>()
        datosProducto["id"] = "$timestamp"
        datosProducto["nombre"] = nombre
        datosProducto["descripcion"] = descripcion
        datosProducto["precio"] = precio
        datosProducto["uidVendedor"] = uidVendedor
        datosProducto["tiempoRegistro"] = timestamp
        datosProducto["imagen"] = "item_imagen"

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child("$timestamp")
            .setValue(datosProducto)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                irAMisProductos()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Error al guardar en BD: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irAMisProductos() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.navFragment, FragmentInicioV())
            .commit()
    }

    private fun limpiarCampos() {
        binding.etNombreProducto.text?.clear()
        binding.etDescripcionProducto.text?.clear()
        binding.etPrecioProducto.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}