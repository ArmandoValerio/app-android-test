package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeworld.tiendavirtualapp9b_avs.MainActivityVendedor
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityLoginVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginV.setOnClickListener {
            validarInformacion()
        }

        binding.tvRegistrarV.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroVendedorActivity::class.java))
        }
    }

    private var email = ""
    private var password = ""

    private fun validarInformacion() {
        email = binding.etEmailV.text.toString().trim()
        password = binding.etPasswordV.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmailV.error = "Ingrese su eMail"
            binding.etEmailV.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailV.error = "Email no válido"
            binding.etEmailV.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPasswordV.error = "Ingrese su password"
            binding.etPasswordV.requestFocus()
        } else {
            LoginVendedor()
        }
    }

    private fun LoginVendedor() {
        progressDialog.setMessage("Verificando datos")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                comprobarTipoUsuarioVendedor()
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

    private fun comprobarTipoUsuarioVendedor() {
        progressDialog.setMessage("Comprobando tipo de usuario")
        val uid = firebaseAuth.uid!!

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val tipoU = snapshot.child("tipoUsuario").value.toString()

                    if (tipoU == "Vendedor") {
                        startActivity(Intent(this@LoginVendedorActivity, MainActivityVendedor::class.java))
                        finishAffinity()
                        Toast.makeText(
                            this@LoginVendedorActivity,
                            "Bienvenida(o) usuario tipo vendedor",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Si la cuenta pertenece a un Cliente, se cierra la sesión
                        firebaseAuth.signOut()
                        Toast.makeText(
                            this@LoginVendedorActivity,
                            "Esta cuenta es de tipo Cliente. Ingrese desde el área de Clientes.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    progressDialog.dismiss()
                    firebaseAuth.signOut()
                    Toast.makeText(
                        this@LoginVendedorActivity,
                        "Error al consultar usuario: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}