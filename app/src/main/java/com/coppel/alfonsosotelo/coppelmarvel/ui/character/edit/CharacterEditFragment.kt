package com.coppel.alfonsosotelo.coppelmarvel.ui.character.edit

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseFragment
import com.coppel.alfonsosotelo.coppelmarvel.databinding.FragmentCharacterEditBinding
import com.coppel.alfonsosotelo.coppelmarvel.ui.SharedViewModel
import com.coppel.alfonsosotelo.coppelmarvel.ui.character.CharacterViewModel
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import kotlinx.android.synthetic.main.fragment_character_edit.*

/**
 * Fragment to edit a Character
 * It needs entityId SafeArgs
 */
class CharacterEditFragment: BaseFragment<FragmentCharacterEditBinding>(R.layout.fragment_character_edit) {
    val viewModel by lazy {
        withViewModel({CharacterViewModel(activity!!.application)}) {
            observe(status, ::handleDataStatus)
        }
    }

    private val sharedViewModel by lazy {
        activity!!.withViewModel({ SharedViewModel(activity!!.application) }) {

        }
    }

    override fun setBindVariables(bind: FragmentCharacterEditBinding) {
        bind.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCharacter(
            CharacterEditFragmentArgs.fromBundle(
                arguments
            ).entityId.toLong()
        )
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {
                if (dataStatus.message == CharacterViewModel.SUCCESS_SAVE) {
                    viewModel.data.value?.let {
                        sharedViewModel.updatedCharacters.addElement(it)
                    }
                    baseContainer.setSnackbar(getString(R.string.saved_successfully))
                }
            }
            DataState.LOADING -> {}
            DataState.ERROR -> {
                dataStatus.message?.let {
                    baseContainer.setSnackbar(it)
                    viewModel.setStatus(DataState.SUCCESS)
                }
            }
        }
    }

    override fun initToolbar() {
        sharedViewModel.toolbarHandler.postValue(
            SharedViewModel.ToolbarHandler(
                getString(R.string.character_edit),
                R.menu.char_edit_menu,
                menuClickListener = {
                    when(it.itemId) {
                        R.id.action_details-> {
                            navigateToDetails()
                        }
                    }
                },
                clear = true to false
            )
        )
    }

    private fun navigateToDetails() {
        navigate(
            CharacterEditFragmentDirections.actionFromCharEditToDetails(
                CharacterEditFragmentArgs.fromBundle(arguments).entityId
            )
        )
    }
}