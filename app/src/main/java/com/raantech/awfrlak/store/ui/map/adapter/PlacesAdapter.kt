package com.raantech.awfrlak.store.ui.map.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.raantech.awfrlak.R
import javax.inject.Inject

class PlacesAdapter @Inject constructor(
        context: Context, items: List<AutocompletePrediction>
) :
        ArrayAdapter<AutocompletePrediction>(context, R.layout.row_place, R.id.tvPlace, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItem: View =
                convertView
                        ?: LayoutInflater.from(context).inflate(R.layout.row_place, parent, false)
        val currentPlace = getItem(position)
        listItem.findViewById<TextView>(R.id.tvPlace)?.text = currentPlace?.getPrimaryText(null)
        return listItem
    }
}