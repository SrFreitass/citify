package com.freitasdev.citify.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freitasdev.citify.R
import com.freitasdev.citify.adapter.CityAdapter
import com.freitasdev.citify.model.db.AppDatabase
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.viewmodel.MainViewModel
import com.google.android.material.textfield.TextInputEditText

class MainFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var repository: CityRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = context?.let { AppDatabase.getInstace(it) }

        if(database != null) {
            repository = CityRepository(database.cityDao())
        }

        mainViewModel = MainViewModel(
            repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getCities()

        val createButton = view.findViewById<FloatingActionButton>(R.id.create_button)
        val buttonSync = view.findViewById<FloatingActionButton>(R.id.sync_button)
        val loading = view.findViewById<TextView>(R.id.loading_text)
        val citiesList = view.findViewById<RecyclerView>(R.id.cities)
        val searchInput = view.findViewById<TextInputEditText>(R.id.search_input)

        val createCityDialog = CreateCityDialogFragment({
            mainViewModel.getCities()
        })

        val cityAdapter =
            CityAdapter(
                requireContext(),
                mutableListOf(),
                {},
                {}
            )

        citiesList.layoutManager = LinearLayoutManager(context)
        citiesList.adapter = cityAdapter

        searchInput?.setOnEditorActionListener { textView, i, keyEvent ->
            mainViewModel.getCityByName(textView?.text.toString())
            false
        }

        buttonSync?.setOnClickListener {
            Log.i("MainFragment", "Click para sincronizar dados")
            Toast.makeText(context, "Sincronizando dados", Toast.LENGTH_SHORT).show()
            mainViewModel.syncData()
        }

        createButton?.setOnClickListener {
            createCityDialog.show(childFragmentManager, "Create City")
        }


        mainViewModel.syncStatus.observe(viewLifecycleOwner, Observer {
            loading.text = mainViewModel.syncStatus.value
        })

        mainViewModel.cities.observe(viewLifecycleOwner, Observer {
            citiesList.adapter = CityAdapter(
                requireContext(),
                mainViewModel.cities.value?.toMutableList()
                 ?: mutableListOf(),
                {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Você tem certeza?")
                        .setMessage("Você ira deletar ao cidade ao clicar em confirmar")
                        .setPositiveButton("Confirmar") { dialog, _ ->
                            mainViewModel.deleteCity(it)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                },
                {
                    val dialog = UpdateCityDialog(it, {
                        mainViewModel.getCities()
                    })
                    dialog.show(childFragmentManager, "Update City")
                }
            )
        })
    }
}