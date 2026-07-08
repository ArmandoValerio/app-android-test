package com.codeworld.tiendavirtualapp9b_avs.cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.whenCreated
import com.codeworld.tiendavirtualapp9b_avs.R
import com.codeworld.tiendavirtualapp9b_avs.cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.codeworld.tiendavirtualapp9b_avs.cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.codeworld.tiendavirtualapp9b_avs.databinding.FragmentInicioCBinding

class FragmentInicioC : Fragment() {

    private lateinit var binding: FragmentInicioCBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInicioCBinding.inflate(inflater, container, false)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ap_tienda_c -> {
                  replaceFragment(FragmentTiendaC())
                }
                R.id.ap_mis_ordenes_c -> {
                    replaceFragment(FragmentMisOrdenesC())
                }
            }
            true
        }
        replaceFragment(FragmentTiendaC())
        binding.bottomNavigationView.selectedItemId = R.id.ap_tienda_c

        return binding.root
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}