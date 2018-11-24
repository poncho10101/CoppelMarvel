package com.coppel.alfonsosotelo.coppelmarvel.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScroll(var visibleThreeshold: Int = 10, var onLoadMore: (() -> Unit)? = null): RecyclerView.OnScrollListener(){
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var previousTotal: Int = 0
    var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                visibleItemCount = it.childCount
                totalItemCount = it.itemCount
                pastVisiblesItems = it.findFirstVisibleItemPosition()

                if (loading && totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreeshold)) {
                    onLoadMore?.invoke()
                    loading = true
                }
            }
        }
    }
}