package com.coppel.alfonsosotelo.coppelmarvel.rx

import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import io.reactivex.disposables.Disposable

/**
 * Class to show a PopupMenu in a View, its RX Disposable
 */
class RxPopupMenu(
        val anchor: View,
        @MenuRes val menuId: Int,
        var forceShowIcon: Boolean = true,
        val listener: ((MenuItem) -> Unit)?
): Disposable {

    private val popup: PopupMenu by lazy { PopupMenu(anchor.context, anchor) }
    private var disposed: Boolean = false

    init {
        show()
        popup.setOnDismissListener {
            disposed = true
        }
    }

    override fun isDisposed(): Boolean {
        return disposed
    }

    override fun dispose() {
        popup.dismiss()
    }

    private fun show() {
        popup.inflate(menuId)

        popup.setOnMenuItemClickListener {
            listener?.invoke(it)
            true
        }

        if (forceShowIcon) {
            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popup)
                mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                popup.show()
            }
        } else {
            popup.show()
        }
    }

}