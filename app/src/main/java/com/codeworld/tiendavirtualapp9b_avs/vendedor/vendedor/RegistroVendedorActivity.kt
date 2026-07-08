package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeworld.tiendavirtualapp9b_avs.Constantes
import com.codeworld.tiendavirtualapp9b_avs.MainActivityVendedor
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Esper leyendo información tipo vendedor")
        progressDialog.setCanceledOnTouchOutside(false)
        //progressDialog esta obsoleto (deprecated)
        //Lineamientos de Material design utilizar : ProgressDialog, CircularProggresDialog

        binding.btnRegistrarV.setOnClickListener {
            validarInformacion()
        }

    }

    private var nombre = ""
    private var email = ""
    private var password = ""
    private var cpassword = ""
    private fun validarInformacion() {
        nombre = binding.etNombreV.text.toString().trim()
        email = binding.etEmailV.text.toString().trim()
        password = binding.etPasswordV.text.toString().trim()
        cpassword = binding.etCPasswordV.text.toString().trim()

        if(nombre.isEmpty()){
            binding.etNombreV.error = "Ingresa nombre completo"
            binding.etNombreV.requestFocus()
        }else if (email.isEmpty()){
            binding.etEmailV.error = "Ingrese su eMail"
            binding.etEmailV.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailV.error = "Email no válido"
            binding.etEmailV.requestFocus()
        }else if (password.isEmpty()){
            binding.etPasswordV.error = "Ingrese su password"
            binding.etPasswordV.requestFocus()
        }else if (password.length < 6 ){
            binding.etPasswordV.error = "Almenos 6 caracteres"
            binding.etPasswordV.requestFocus()
        }else if (cpassword.isEmpty()){
            binding.etCPasswordV.error = "Ingrese su password"
            binding.etCPasswordV.requestFocus()
        }else if (password != cpassword){
            binding.etCPasswordV.error = "No coincide su password"
            binding.etCPasswordV.requestFocus()
        }else {
            registrarVendedor()
        }
    }

    private fun registrarVendedor() {
        progressDialog.setMessage("Creando su cuenta...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                insertarInformacionBD()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falló el registro debido ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInformacionBD() {
        progressDialog.setMessage("Guardando información en la BD")

        val uidBD = firebaseAuth.uid
        val nombreBD = nombre
        val emailBD = email
        //val tipoUsuario = "Vendedor"
        val tiempoBD = Constantes().obtenerTipoD()

        val datosVendedor = HashMap<String, Any>()

        datosVendedor["uid"] = "$uidBD"
        datosVendedor["nombre"] = "$nombreBD"
        datosVendedor["email"] = "$emailBD"
        datosVendedor["tipoUsuario"] = "Vendedor"
        datosVendedor["tiempo_Registro"] = tiempoBD

        val references = FirebaseDatabase.getInstance().getReference("Usuarios")
        references.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Falló el registro en BD debido a: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
//val passwordBD = password