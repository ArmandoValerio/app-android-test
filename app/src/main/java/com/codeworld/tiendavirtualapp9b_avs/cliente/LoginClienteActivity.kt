package com.codeworld.tiendavirtualapp9b_avs.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginC.setOnClickListener {
            validarInformacion()
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroClienteActivity::class.java))
        }
    }

    private var email = ""
    private var password = ""

    private fun validarInformacion() {
        email = binding.etEmailC.text.toString().trim()
        password = binding.etPasswordC.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmailC.error = "Ingrese su eMail"
            binding.etEmailC.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailC.error = "Email no válido"
            binding.etEmailC.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPasswordC.error = "Ingrese su password"
            binding.etPasswordC.requestFocus()
        } else {
            loginCliente()
        }
    }

    private fun loginCliente() {
        progressDialog.setMessage("Verificando credenciales")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                comprobarTipoUsuarioCliente()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo iniciar sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun comprobarTipoUsuarioCliente() {
        progressDialog.setMessage("Comprobando tipo de usuario")
        val uid = firebaseAuth.uid!!

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val tipoU = snapshot.child("tipoUsuario").value.toString()

                    if (tipoU == "Cliente") {
                        startActivity(Intent(this@LoginClienteActivity, MainActivityCliente::class.java))
                        finishAffinity()
                        Toast.makeText(
                            this@LoginClienteActivity,
                            "Bienvenida(o) usuario tipo cliente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Si la cuenta pertenece a un Vendedor, se cierra la sesión
                        firebaseAuth.signOut()
                        Toast.makeText(
                            this@LoginClienteActivity,
                            "Esta cuenta es de tipo Vendedor. Ingrese desde el área de Vendedores.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    progressDialog.dismiss()
                    firebaseAuth.signOut()
                    Toast.makeText(
                        this@LoginClienteActivity,
                        "Error al consultar usuario: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}