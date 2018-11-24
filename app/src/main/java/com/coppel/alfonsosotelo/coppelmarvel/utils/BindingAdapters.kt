package com.coppel.alfonsosotelo.coppelmarvel.utils

import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.coppel.alfonsosotelo.coppelmarvel.GlideApp
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseAdapter
import com.coppel.alfonsosotelo.coppelmarvel.ui.SharedViewModel
import com.google.android.material.textfield.TextInputEditText


/**
 * Binds TextInputLayout
 * @param errorText set this error to InputLayout
 */
@BindingAdapter("error")
fun bindTextInputLayout(view: TextInputEditText, errorText: String?) {
    view.setLayoutError(errorText)
}

/**
 * Binds RecyclerView
 * @param adapter Adapter to put in RecyclerView, needs to extends from BaseAdapter
 * @param adapterItems List of items to put in adapter
 * @param dividerItemOrientation If you want a DividerItemDecoration pass the Orientation
 * @param onLoadMore lambda called with EndlessScroll
 */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("adapter", "adapterItems", "dividerItemOrientation", "onLoadMore")
fun bindRecyclerView(view: RecyclerView, adapter: BaseAdapter<*, *>?, adapterItems: List<Any>?, dividerItemOrientation: Int?, onLoadMore: (() -> Unit)?){
    (adapter as? BaseAdapter<Any, *>)?.submitList(adapterItems)
    dividerItemOrientation?.let {
        view.addItemDecoration(DividerItemDecoration(view.context, it))
    }
    if (view.adapter == null)
        view.adapter = adapter

    view.clearOnScrollListeners()
    onLoadMore?.let {
        view.addOnScrollListener(EndlessScroll {
            it.invoke()
        })
    }
}

/**
 * Binds ImageView
 * @param srcUrl setImage to ImageView from an Url using Glide
 */
@BindingAdapter("srcUrl")
fun bindImageView(view: ImageView, srcUrl: String?) {
    srcUrl?.let {
        GlideApp.with(view.context)
            .load(it)
            .placeholder(CircularProgressDrawable(view.context).apply { start() })
            .error(0)
            .into(view)
    }
}

/**
 * Binds Toolbar
 * @param toolbarHandler Toolbar Data to put in the Toolbar View
 */
@BindingAdapter("toolbarHandler")
fun bindToolbar(view: Toolbar, toolbarHandler: SharedViewModel.ToolbarHandler?) {
    toolbarHandler?.clear?.takeIf { it.first }?.let {
        view.title = ""
        view.menu?.clear()
        view.setOnMenuItemClickListener(null)
        if (it.second) {
            view.navigationIcon = null
            view.setNavigationOnClickListener(null)
        }
    }

    toolbarHandler?.title?.let { view.title = it }
    toolbarHandler?.menuRes?.let { view.inflateMenu(it) }
    toolbarHandler?.drawableRes?.let { view.setNavigationIcon(it) }
    toolbarHandler?.menuClickListener?.let { listener ->
        view.setOnMenuItemClickListener{
            listener.invoke(it)
            true
        }
    }
    toolbarHandler?.navigationClickListener?.let { listener ->
        view.setNavigationOnClickListener {
            listener.invoke()
        }
    }
}
