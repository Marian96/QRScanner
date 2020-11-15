package com.marianpusk.qrscanner.ui.main.generatecodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marianpusk.qrscanner.database.entities.GeneratedCodeEntity
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import com.marianpusk.qrscanner.databinding.CodesBinding
import com.marianpusk.qrscanner.databinding.GeneratedCodesBinding

class GeneratedCodeRecycleAdapter (val clickListener: GeneratedCodeListener) : ListAdapter<GeneratedCodeEntity, GeneratedCodeRecycleAdapter.CodeViewHolder>(GeneratedCodeDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeViewHolder {
        return CodeViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: CodeViewHolder, position: Int) {
        when(holder) {
            is CodeViewHolder -> {

                holder.bind(getItem(position),clickListener)
            }
        }

    }


    class CodeViewHolder(
        val binding: GeneratedCodesBinding
    ): RecyclerView.ViewHolder(binding.root) {


        fun bind(
            item: GeneratedCodeEntity,
            clickListener: GeneratedCodeListener
        ) {
            binding.clickListener = clickListener
            binding.code = item
            binding.executePendingBindings()


        }

        companion object {
            fun from(parent: ViewGroup): CodeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GeneratedCodesBinding.inflate(layoutInflater,parent,false)
                return CodeViewHolder(binding)
            }
        }
    }

}

class GeneratedCodeDiffCallback : DiffUtil.ItemCallback<GeneratedCodeEntity>() {
    override fun areItemsTheSame(oldItem: GeneratedCodeEntity, newItem: GeneratedCodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GeneratedCodeEntity, newItem: GeneratedCodeEntity): Boolean {
        return oldItem == newItem
    }
}

class GeneratedCodeListener(val clickListener: (value: String, id: Int) -> Unit){

    fun onClick(code: GeneratedCodeEntity) = clickListener(code.value, code.id)
}


class DeleteCodeListener(val clickListener: (id: Int) -> Unit){

    fun onClick(code: GeneratedCodeEntity) = clickListener(code.id)
}

