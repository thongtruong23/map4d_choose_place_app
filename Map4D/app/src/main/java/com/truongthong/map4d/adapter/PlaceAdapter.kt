package com.truongthong.map4d.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.truongthong.map4d.databinding.ItemPlaceBinding
import com.truongthong.map4d.models.PlaceResult

class PlaceAdapter(val clickListener: ItemClickListener) :
    ListAdapter<PlaceResult, PlaceAdapter.PlaceViewHolder>(PlacelDiffCallback()) {

    class PlacelDiffCallback : DiffUtil.ItemCallback<PlaceResult>() {
        override fun areItemsTheSame(oldItem: PlaceResult, newItem: PlaceResult): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: PlaceResult, newItem: PlaceResult): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class PlaceViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceResult, clickListener: ItemClickListener) {
            binding.place = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PlaceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPlaceBinding.inflate(layoutInflater, parent, false)

                return PlaceViewHolder(binding)
            }
        }
    }

}

class ItemClickListener(val clickListener: (placeResult: PlaceResult) -> Unit) {
    fun onClick(placeResult: PlaceResult) = clickListener(placeResult)
}