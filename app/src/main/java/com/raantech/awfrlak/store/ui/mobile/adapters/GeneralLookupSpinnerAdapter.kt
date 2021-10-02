package com.raantech.awfrlak.store.ui.mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.RowSpinnerItemBinding
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView

class GeneralLookupSpinnerAdapter(
    private val powerSpinnerView: PowerSpinnerView,
    private val context: Context
) : RecyclerView.Adapter<GeneralLookupSpinnerAdapter.ViewHolder>(),
    PowerSpinnerInterface<GeneralLookup> {

    fun getSelectedItem():GeneralLookup{
        return spinnerItems[index]
    }
    override var index: Int = powerSpinnerView.selectedIndex
    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<GeneralLookup>? =
        null
    private val compoundPadding: Int = 12
    val spinnerItems: MutableList<GeneralLookup> = arrayListOf()
    private val NO_SELECTED_INDEX = -1

    init {
        this.spinnerView.compoundDrawablePadding = compoundPadding
    }

    class ViewHolder(val view: RowSpinnerItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<RowSpinnerItemBinding>(
            inflater,
            R.layout.row_spinner_item, parent, false
        )
        powerSpinnerView.setIsFocusable(true)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.data = spinnerItems[position].name
        if (index == position)
            holder.view.spinnerTarget.setTextColor(context.resources.getColor(R.color.button_color))
        else
            holder.view.spinnerTarget.setTextColor(context.resources.getColor(R.color.defaultTextColor))
        holder.itemView.setOnClickListener {
            notifyItemSelected(position)
        }
    }

    override fun notifyItemSelected(index: Int) {
        if (index == NO_SELECTED_INDEX) return
        val item = spinnerItems[index]
        val oldIndex = this.index
        this.index = index
        item.name?.let { this.spinnerView.notifyItemSelected(index, it) }
        notifyDataSetChanged()
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != NO_SELECTED_INDEX }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = item
        )
    }

    override fun setItems(itemList: List<GeneralLookup>) {
        this.spinnerItems.clear()
        this.spinnerItems.addAll(itemList)
        notifyDataSetChanged()
    }


    override fun getItemCount() = this.spinnerItems.size
}