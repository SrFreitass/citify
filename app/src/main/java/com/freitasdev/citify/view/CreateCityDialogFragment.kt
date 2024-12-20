package com.freitasdev.citify.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.freitasdev.citify.R
import com.freitasdev.citify.model.db.AppDatabase
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.viewmodel.CreateCityDialogViewModel
import com.google.android.material.textfield.TextInputEditText


class CreateCityDialogFragment(private val onCreateCity: () -> Unit) : DialogFragment() {
    private lateinit var repository: CityRepository
    private lateinit var viewModel: CreateCityDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val appDatabase = AppDatabase.getInstace(requireContext())

        super.onCreate(savedInstanceState)

        repository = CityRepository(appDatabase.cityDao())
        viewModel = CreateCityDialogViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_city_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val city = view.findViewById<TextInputEditText>(R.id.city_input)
        val uf = view.findViewById<Spinner>(R.id.state_input)
        val region = view.findViewById<Spinner>(R.id.region_input)
        val createButton = view.findViewById<Button>(R.id.create_city_btn)



        createButton.setOnClickListener{
            if(city?.text?.toString()?.isEmpty() == true) {
                Toast.makeText(requireContext(), "Cidade inválida", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val states = when (region.selectedItem.toString()) {
                "Centro-Oeste" -> resources.getStringArray(R.array.Centro_oeste)
                "Nordeste" -> resources.getStringArray(R.array.Nordeste)
                "Norte" -> resources.getStringArray(R.array.Norte)
                "Sudeste" -> resources.getStringArray(R.array.Sudeste)
                "Sul" -> resources.getStringArray(R.array.Sul)
                else -> resources.getStringArray(R.array.Centro_oeste)
            }

            if(states.indexOf(uf.selectedItem.toString()) == -1) {
                Toast.makeText(requireContext(), "Estado inválido não corresponde a região", Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }

            Log.i("CreateCity", "$states")

            viewModel.createCity(
                CityEntity(
                    name = city.text?.trim().toString(),
                    uf = uf.selectedItem.toString(),
                    region = region.selectedItem.toString()
                )
            )

            city.text = null
            Toast.makeText(requireContext(), "Carregando...", Toast.LENGTH_LONG).show()
        }

        viewModel.createdStatus.observe(viewLifecycleOwner, Observer {

            if(!it) {
                Toast.makeText(requireContext(), "Cidade inválida (Já existente)", Toast.LENGTH_LONG).show()
                return@Observer
            }

            Toast.makeText(requireContext(), "Cidade criada", Toast.LENGTH_LONG).show()
            onCreateCity()
            dialog?.dismiss()
        })
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

}