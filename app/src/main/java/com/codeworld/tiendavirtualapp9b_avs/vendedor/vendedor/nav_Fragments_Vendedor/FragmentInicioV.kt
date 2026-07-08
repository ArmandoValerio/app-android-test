package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.nav_Fragments_Vendedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codeworld.tiendavirtualapp9b_avs.R
import com.codeworld.tiendavirtualapp9b_avs.databinding.FragmentInicioVBinding
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Bottom_Nav_Fragment.FragmentMisOrdenesV
import com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Bottom_Nav_Fragment.FragmentMisProductosV

class FragmentInicioV : Fragment() {


    private lateinit var binding : FragmentInicioVBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInicioVBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ap_mis_productos_v -> {
                    replaceFragment(FragmentMisProductosV())
                }
                R.id.ap_mis_ordenes_v -> {
                    replaceFragment(FragmentMisOrdenesV())
                }
            }
            true
        }
        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.ap_mis_productos_v

        binding.addFab.setOnClickListener{
            Toast.makeText(mContext, "Presionaste Agregar Productos (botón flotante)", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }
}

