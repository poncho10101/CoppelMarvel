package com.coppel.alfonsosotelo.coppelmarvel.ui.character.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseFragment
import com.coppel.alfonsosotelo.coppelmarvel.databinding.FragmentCharacterDetailsBinding
import com.coppel.alfonsosotelo.coppelmarvel.ui.SharedViewModel
import com.coppel.alfonsosotelo.coppelmarvel.ui.character.CharacterViewModel
import com.coppel.alfonsosotelo.coppelmarvel.utils.*

/**
 * Fragment to show details of a Character
 * It needs entityId SafeArgs
 */
class CharacterDetailsFragment: BaseFragment<FragmentCharacterDetailsBinding>(R.layout.fragment_character_details) {
    val viewModel by lazy {
        withViewModel({ CharacterViewModel(activity!!.application) }) {
            observe(status, ::handleDataStatus)
        }
    }

    val sharedViewModel by lazy {
        activity!!.withViewModel({SharedViewModel(activity!!.application)}) {

        }
    }

    override fun setBindVariables(bind: FragmentCharacterDetailsBinding) {
        bind.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCharacter(
            CharacterDetailsFragmentArgs.fromBundle(
                arguments
            ).entityId.toLong()
        )
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {}
            DataState.LOADING -> {}
            DataState.ERROR -> {}
        }
    }

    override fun initToolbar() {
        sharedViewModel.toolbarHandler.postValue(
            SharedViewModel.ToolbarHandler(
                getString(R.string.character_details),
                R.menu.char_details_menu,
                menuClickListener = {
                    when(it.itemId) {
                        R.id.action_edit -> {
                            navigateToEdit()
                        }
                    }
                },
                clear = true to false
            )
        )
    }

    private fun navigateToEdit() {
        navigate(
            CharacterDetailsFragmentDirections.actionFromCharDetailsToEdit(
                CharacterDetailsFragmentArgs.fromBundle(arguments).entityId
            )
        )
    }
}