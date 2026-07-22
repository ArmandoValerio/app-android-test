package com.codeworld.tiendavirtualapp9b_avs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codeworld.tiendavirtualapp9b_avs.cliente.LoginClienteActivity
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivitySeleccionarTipoMainBinding
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.LoginVendedorActivity

class SeleccionarTipoMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionarTipoMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Evento para ir al Login de Vendedor
        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this, LoginVendedorActivity::class.java))
        }

        // Evento para ir al Login de Cliente
        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this, LoginClienteActivity::class.java))
        }
    }
}