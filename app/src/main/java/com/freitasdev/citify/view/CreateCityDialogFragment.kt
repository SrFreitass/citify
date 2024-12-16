package com.freitasdev.citify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
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
        val uf = view.findViewById<TextInputEditText>(R.id.state_input)
        val region = view.findViewById<TextInputEditText>(R.id.region_input)
        val createButton = view.findViewById<Button>(R.id.create_city_btn)

        createButton.setOnClickListener{
            viewModel.createCity(
                CityEntity(
                    name = city.text.toString(),
                    uf = uf.text.toString(),
                    region = region.text.toString()
                )
            )

            dialog?.dismiss()
            onCreateCity()
        }
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