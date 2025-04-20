package com.example.shopit.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class verticalitemdecoration(private val amount:Int ):RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom=amount
    }
}