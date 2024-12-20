package com.freitasdev.citify.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast
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
        val regionFilter = view.findViewById<RadioGroup>(R.id.region_filter)
        val stateFilter = view.findViewById<Spinner>(R.id.state_filter)
        val removeFiltersButton = view.findViewById<FloatingActionButton>(R.id.remove_filters_btn)

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

        fun Spinner.selected(action: (position: Int) -> Unit) {
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    action(p2)
                }
            }
        }

        fun removeFilters() {
            stateFilter.setSelection(0)
            searchInput.setText("")
            regionFilter.clearCheck()
        }

        removeFiltersButton.setOnClickListener {
            removeFilters()
            Toast.makeText(requireContext(), "Filtro removido", Toast.LENGTH_LONG).show()
        }

        stateFilter.selected {
            if(regionFilter.isSelected && it == 0) {
                return@selected
            }

            val state = when(it) {
                in 1..26 -> resources.getStringArray(R.array.uf_options_filter)[it]
                else -> null
            }

            if(state != null) {
                mainViewModel.getCityByState(state)
            }

            searchInput.setText("")
        }



        regionFilter?.setOnCheckedChangeListener { radioGroup, i ->
            Log.i("MainFragment", "filtrou por região")
            Log.i("MainFragment", "${i}")
            val region = when (i) {
                1 -> "Centro-Oeste"
                2 -> "Sul"
                3 -> "Norte"
                4 -> "Nordeste"
                5 -> "Sudeste"
                else -> {
                    "Centro-Oeste"
                }
            }

            mainViewModel.getCityByRegion(region)
            if(i == 0) {
                searchInput.setText("")
            }
            stateFilter.setSelection(0)
        }

        searchInput?.setOnEditorActionListener { textView, i, keyEvent ->
            mainViewModel.getCityByName(textView?.text.toString())
            stateFilter.setSelection(0)
            false
        }

        buttonSync?.setOnClickListener {
            removeFilters()
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