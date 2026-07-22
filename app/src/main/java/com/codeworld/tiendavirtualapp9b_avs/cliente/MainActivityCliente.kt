package com.codeworld.tiendavirtualapp9b_avs.cliente

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.codeworld.tiendavirtualapp9b_avs.R
import com.codeworld.tiendavirtualapp9b_avs.cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.codeworld.tiendavirtualapp9b_avs.cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.codeworld.tiendavirtualapp9b_avs.cliente.Nav_Fragments_Cliente.FragmentInicioC
import com.codeworld.tiendavirtualapp9b_avs.cliente.Nav_Fragments_Cliente.FragmentMiPerfilC
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        replaceFragment(FragmentInicioC())
        binding.navigationView.setCheckedItem(R.id.ap_inicio_c)
    }

    private fun cerrarSesion() {
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, LoginClienteActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "Se cerró la sesión del cliente", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        if (firebaseAuth!!.currentUser == null) {
            startActivity(Intent(applicationContext, LoginClienteActivity::class.java))
            finish()
        } else {
            Toast.makeText(applicationContext, "Cliente en línea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ap_inicio_c -> {
                replaceFragment(FragmentInicioC())
            }
            R.id.ap_mi_perfil_c -> {
                replaceFragment(FragmentMiPerfilC())
            }
            R.id.ap_cerrar_sesion_c -> {
                cerrarSesion()
            }
            R.id.ap_tienda_c -> {
                replaceFragment(FragmentTiendaC())
            }
            R.id.ap_mis_ordenes_c -> {
                replaceFragment(FragmentMisOrdenesC())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}