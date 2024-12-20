package com.freitasdev.citify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.freitasdev.citify.R
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.freitasdev.citify.model.db.AppDatabase
import com.freitasdev.citify.model.entities.CityEntity
import com.freitasdev.citify.repository.CityRepository
import com.freitasdev.citify.viewmodel.UpdateCityDialogViewModel
import com.google.android.material.textfield.TextInputEditText

class UpdateCityDialog(private val cityId: Int, private val onUpdate: () -> Unit) : DialogFragment() {
    private lateinit var viewModel: UpdateCityDialogViewModel
    private lateinit var repository: CityRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = context?.let { AppDatabase.getInstace(it) }

        if(database != null) {
            repository = CityRepository(database.cityDao())
        }

        viewModel = UpdateCityDialogViewModel(
            repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_city_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCityById(cityId)

        val cityInput = view.findViewById<TextInputEditText>(R.id.city_input)
        val stateInput = view.findViewById<Spinner>(R.id.state_input)
        val regionInput = view.findViewById<Spinner>(R.id.region_input)
        val saveButton = view.findViewById<Button>(R.id.save_update)

        saveButton.setOnClickListener{
            val states = when (regionInput.selectedItem.toString()) {
                "Centro-Oeste" -> resources.getStringArray(R.array.Centro_oeste)
                "Nordeste" -> resources.getStringArray(R.array.Nordeste)
                "Norte" -> resources.getStringArray(R.array.Norte)
                "Sudeste" -> resources.getStringArray(R.array.Sudeste)
                "Sul" -> resources.getStringArray(R.array.Sul)
                else -> resources.getStringArray(R.array.Centro_oeste)
            }

            if(states.indexOf(stateInput.selectedItem.toString()) == -1) {
                Toast.makeText(requireContext(), "Estado inválido não corresponde a região", Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }


            viewModel.updateCity(
                CityEntity(
                    id = cityId,
                    name = cityInput?.text.toString(),
                    uf = stateInput?.selectedItem.toString(),
                    region = regionInput?.selectedItem.toString()
                )
            )
 
            onUpdate()
            dialog?.dismiss()
        }

        viewModel.city.observe(viewLifecycleOwner, Observer {
            cityInput.setText(it?.name)

            val stateArray = resources.getStringArray(R.array.uf_options)
            val regionArray = resources.getStringArray(R.array.region_options)

            val statePosition = stateArray.indexOf(it?.uf)
            val regionPosition = regionArray.indexOf(it?.region)

            stateInput.setSelection(statePosition)
            regionInput.setSelection(regionPosition)
        })

    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }
}