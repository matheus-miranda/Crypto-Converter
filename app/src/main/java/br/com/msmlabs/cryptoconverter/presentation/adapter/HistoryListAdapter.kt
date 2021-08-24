package br.com.msmlabs.cryptoconverter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.databinding.ItemHistoryBinding

class HistoryListAdapter :
    ListAdapter<GeckoResponseEntity, HistoryListAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemHistoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GeckoResponseEntity) {
            binding.tvName.text = item.types
            binding.tvValue.text = item.currentPrice.toString()
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<GeckoResponseEntity>() {
    override fun areItemsTheSame(
        oldItem: GeckoResponseEntity,
        newItem: GeckoResponseEntity
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: GeckoResponseEntity,
        newItem: GeckoResponseEntity
    ): Boolean = oldItem == newItem
}
