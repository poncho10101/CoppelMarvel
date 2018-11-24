package com.coppel.alfonsosotelo.coppelmarvel.ui.characterlist

import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.coppel.alfonsosotelo.coppelmarvel.GlideApp
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseAdapter
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import kotlinx.android.synthetic.main.row_character_list.view.*

/**
 * Adapter of a List of Character, inherits from BaseAdapter
 */
class CharacterListAdapter: BaseAdapter<Character, CharacterListAdapter.ViewHolder>(R.layout.row_character_list, DefaultDiffCallback()) {
    override fun onBindVH(holder: ViewHolder, position: Int) {

    }

    override fun instanceViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): BaseViewHolder(view) {
        override fun bind(element: Character) {
            itemView.apply {
                tvName.text = element.name
                tvDescription.text = element.description

                element.thumbnail?.let {
                    GlideApp.with(context)
                        .load(it.getFullPath())
                        .placeholder(CircularProgressDrawable(context).apply { start() })
                        .error(0)
                        .into(ivThumbnail)
                }
            }
        }

    }
}