package com.freitasdev.citify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freitasdev.citify.R
import com.freitasdev.citify.adapter.CityAdapter
import com.freitasdev.citify.model.db.AppDatabase
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.viewmodel.MainViewModel

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

        val buttonSync = view.findViewById<FloatingActionButton>(R.id.sync_button)
        val loading = view.findViewById<TextView>(R.id.loading_text)
        val citiesList = view.findViewById<RecyclerView>(R.id.cities)


        val cityAdapter =
            CityAdapter(
                requireContext(),
                mutableListOf()
            )

        citiesList.layoutManager = LinearLayoutManager(context)
        citiesList.adapter = cityAdapter

        buttonSync?.setOnClickListener {
            Log.i("MainFragment", "Click para sincronizar dados")
            Toast.makeText(context, "Sincronizando dados", Toast.LENGTH_SHORT).show()
            mainViewModel.syncData()
        }

        mainViewModel.syncStatus.observe(viewLifecycleOwner, Observer {
            loading.text = mainViewModel.syncStatus.value
        })

        mainViewModel.cities.observe(viewLifecycleOwner, Observer {
            citiesList.adapter = mainViewModel.cities.value?.let { it1 ->
                CityAdapter(
                    requireContext(),
                    it1
                )
            }
        })

    }
}