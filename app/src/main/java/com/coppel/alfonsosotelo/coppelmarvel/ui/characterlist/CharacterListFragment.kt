package com.coppel.alfonsosotelo.coppelmarvel.ui.characterlist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseAdapter
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseFragment
import com.coppel.alfonsosotelo.coppelmarvel.databinding.FragmentCharacterListBinding
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import com.coppel.alfonsosotelo.coppelmarvel.rx.RxPopupMenu
import com.coppel.alfonsosotelo.coppelmarvel.ui.SharedViewModel
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import kotlinx.android.synthetic.main.fragment_character_list.*

class CharacterListFragment: BaseFragment<FragmentCharacterListBinding>(R.layout.fragment_character_list) {

    private val viewModel by lazy {
        withViewModel({CharacterListViewModel(activity!!.application)}){
            observe(status, ::handleDataStatus)
        }
    }

    private val adapter by lazy { // CharacterListAdapter loaded lazily
        CharacterListAdapter().apply {
            getClickObservable().subscribe(::onClickItem).addToDisposables(disposables)
            getLongClickObservable().subscribe(::onLongClickItem).addToDisposables(disposables)
        }
    }

    private val sharedViewModel by lazy {
        activity!!.withViewModel({SharedViewModel(activity!!.application)}) {
            observe(updatedCharacters, ::updateCharacter)
        }
    }


    override fun setBindVariables(bind: FragmentCharacterListBinding) {
        bind.adapter = adapter // Adds adapter to Bind adapter Variable
        bind.viewModel = viewModel // Adds viewModel to Bind viewModel Variable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // loads the SharedViewModel

        updateCharacter(sharedViewModel.updatedCharacters.value)
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {}
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
                getString(R.string.characters),
                R.menu.char_list_menu,
                menuClickListener = {
                    when(it.itemId) {
                        R.id.action_logout -> {
                            sharedViewModel.logout()
                        }
                    }
                },
                clear = true to false
            )
        )
    }

    /**
     * This method gets the updated characters from the SharedViewModel and update the list in CharacterListViewModel
     */
    private fun updateCharacter(updatedCharacters: List<Character>?) {
        updatedCharacters?.forEach {
            viewModel.updateCharacter(it)
        }
        sharedViewModel.updatedCharacters.postValue(listOf())
    }

    private fun deleteCharacter(deleteCharacter: Character) {
        viewModel.deleteCharacter(deleteCharacter)
    }

    /**
     * When click a record sends to Details
     */
    private fun onClickItem(clickListener: BaseAdapter<Character, CharacterListAdapter.ViewHolder>.ClickListener) {
        navigateToDetails(clickListener.entity.id)
    }

    /**
     * When long click a record show a popup
     */
    private fun onLongClickItem(clickListener: BaseAdapter<Character, CharacterListAdapter.ViewHolder>.ClickListener) {
        RxPopupMenu(clickListener.viewHolder.itemView, R.menu.char_list_long_click_menu) {
            when(it.itemId) {
                R.id.action_details -> navigateToDetails(clickListener.entity.id)
                R.id.action_edit -> navigateToEdit(clickListener.entity.id)
                R.id.action_delete -> deleteCharacter(clickListener.entity)
            }
        }
    }

    private fun navigateToDetails(id: Long) {
        navigate(CharacterListFragmentDirections.actionFromCharListToDetails(id.toInt()))
    }

    private fun navigateToEdit(id: Long) {
        navigate(CharacterListFragmentDirections.actionFromCharListToEdit(id.toInt()))
    }
}