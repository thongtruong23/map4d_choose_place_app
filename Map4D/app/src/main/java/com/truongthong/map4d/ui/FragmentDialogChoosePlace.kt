package com.truongthong.map4d.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.truongthong.map4d.R
import com.truongthong.map4d.adapter.PlaceAdapter
import com.truongthong.map4d.adapter.ItemClickListener
import com.truongthong.map4d.databinding.FragmentDialogPlaceBinding
import com.truongthong.map4d.viewmodel.DialogPlaceViewModel

class FragmentDialogChoosePlace : DialogFragment() {

    private lateinit var dataBinding: FragmentDialogPlaceBinding
    private lateinit var typePlacePlace: TypePlace
    private var codePlace: String? = null
    private lateinit var placeUpdate: PlaceUpdate
    private lateinit var dialogPlaceViewModel: DialogPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        placeUpdate = activity as PlaceUpdate

        dialogPlaceViewModel = ViewModelProvider(this).get(DialogPlaceViewModel::class.java)

        bundle?.let {
            typePlacePlace = it.getParcelable("TYPE")!!
            codePlace = it.getString("CODE").toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_place, container, false)
        dataBinding.viewModel = dialogPlaceViewModel
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogPlaceViewModel.setType(typePlacePlace, codePlace!!)

        handlePlaceAdapter()
        handlePlaceTitle()

        dialogPlaceViewModel.btnCloseState.observe(viewLifecycleOwner, {
            if (it) {
                dismiss()
                dialogPlaceViewModel.onCloseButtonClickSuccess()
            }
        })

    }

    private fun handlePlaceTitle() {
        dialogPlaceViewModel.typePlace.observe(viewLifecycleOwner, {

            val title: String = when (it.type) {
                TypePlace.PROVINCE.type -> {
                    getString(R.string.choose_province)
                }
                TypePlace.DISTRICT.type -> {
                    getString(R.string.choose_district)
                }
                TypePlace.SUBDISTRICT.type -> {
                    getString(R.string.choose_subdistrict)
                }
                else -> getString(R.string.choose_province)
            }
            // println("title: $title")
            dataBinding.chooseAreaTitle.text = title
        })
    }

    private fun handlePlaceAdapter() {
        val adapter = PlaceAdapter(ItemClickListener {
            when (it.type) {
                TypePlace.PROVINCE.type -> {
                    placeUpdate.dataListener(TypePlace.PROVINCE, it)
                    dismiss()
                }
                TypePlace.DISTRICT.type -> {
                    placeUpdate.dataListener(TypePlace.DISTRICT, it)
                    dismiss()
                }
                TypePlace.SUBDISTRICT.type -> {
                    placeUpdate.dataListener(TypePlace.SUBDISTRICT, it)
                    dismiss()
                }
            }
        })

        dataBinding.rvSearch.adapter = adapter

        dialogPlaceViewModel.placeResult.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.submitList(it)
            }
        })
    }

    override fun getTheme(): Int {
        return R.style.ThemeDialogPlace
    }

}