package com.codeworld.tiendavirtualapp9b_avs.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityRegistroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        // Evento del botón registrar cliente
        binding.btnRegistrarC.setOnClickListener {
            validarInformacion()
        }
    }

    private var nombres = ""
    private var email = ""
    private var password = ""
    private var cPassword = ""

    private fun validarInformacion() {
        nombres = binding.etNombreC.text.toString().trim()
        email = binding.etEmailC.text.toString().trim()
        password = binding.etPasswordC.text.toString().trim()
        cPassword = binding.etCPasswordC.text.toString().trim()

        if (nombres.isEmpty()) {
            binding.etNombreC.error = "Ingrese sus nombres"
            binding.etNombreC.requestFocus()
        } else if (email.isEmpty()) {
            binding.etEmailC.error = "Ingrese su eMail"
            binding.etEmailC.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailC.error = "Email no válido"
            binding.etEmailC.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPasswordC.error = "Ingrese su password"
            binding.etPasswordC.requestFocus()
        } else if (password.length < 6) {
            binding.etPasswordC.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPasswordC.requestFocus()
        } else if (cPassword.isEmpty()) {
            binding.etCPasswordC.error = "Confirme su password"
            binding.etCPasswordC.requestFocus()
        } else if (password != cPassword) {
            binding.etCPasswordC.error = "Las contraseñas no coinciden"
            binding.etCPasswordC.requestFocus()
        } else {
            registrarCliente()
        }
    }

    private fun registrarCliente() {
        progressDialog.setMessage("Creando cuenta...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                guardarInformacionBD()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo crear la cuenta debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun guardarInformacionBD() {
        progressDialog.setMessage("Guardando información de usuario...")

        val uid = firebaseAuth.uid
        val datosCliente = HashMap<String, Any>()
        datosCliente["uid"] = uid!!
        datosCliente["nombres"] = nombres
        datosCliente["email"] = email
        datosCliente["tipoUsuario"] = "Cliente" // Identificador para la consulta en SplashScreenActivity

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(
                    this,
                    "Cuenta creada exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo guardar la información debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}