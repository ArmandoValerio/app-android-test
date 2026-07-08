package com.codeworld.tiendavirtualapp9b_avs

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codeworld.tiendavirtualapp9b_avs.databinding.ActivityMainVendedorBinding
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Bottom_Nav_Fragment.FragmentMisOrdenesV
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Bottom_Nav_Fragment.FragmentMisProductosV
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.LoginVendedorActivity
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.nav_Fragments_Vendedor.FragmentMiTiendaV
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.nav_Fragments_Vendedor.FragmentReseniasV
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.RegistroVendedorActivity
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.nav_Fragments_Vendedor.FragmentInicioV
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainVendedorBinding
    private var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth =  FirebaseAuth.getInstance()
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

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.ap_inicio_v)

    }

    private fun cerrarSesion (){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "Se cerró la sesión del vendedor", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        if (firebaseAuth!!.currentUser == null){
            startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
            //Toast.makeText(applicationContext, "Vendedor no registrado o logueado", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(applicationContext, "Vendedor en línea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ap_inicio_v ->{
                replaceFragment(FragmentInicioV())
            }
            R.id.ap_mi_tienda_v -> {
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.ap_resenias_v -> {
                replaceFragment(FragmentReseniasV())
            }
            R.id.ap_cerrar_sesion_v -> {
                cerrarSesion()
                //Toast.makeText(applicationContext, "Saliste de la aplicación", Toast.LENGTH_SHORT).show()
            }
            R.id.ap_mis_productos_v -> {
                replaceFragment(FragmentMisProductosV())
            }
            R.id.ap_mis_ordenes_v -> {
                replaceFragment(FragmentMisOrdenesV())
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}