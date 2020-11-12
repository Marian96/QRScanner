package com.marianpusk.qrscanner.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marianpusk.qrscanner.database.entities.QRCodeEntity
import com.marianpusk.qrscanner.databinding.CodesBinding

class QRCodeRecycleAdapter (val clickListener: QRCodeListener,
                            val delete: DeleteCodeListener) : ListAdapter<QRCodeEntity, QRCodeRecycleAdapter.CodeViewHolder>(TrainingPlandDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeViewHolder {
        return CodeViewHolder.from(parent)
    }

//    override fun getItemCount(): Int {
//        return items.size
//    }


//    fun submitList(planList:List<TrainingPlanEntity>) {
//        items = planList
//    }

    override fun onBindViewHolder(holder: CodeViewHolder, position: Int) {
        when(holder) {
            is CodeViewHolder -> {

                holder.bind(getItem(position),clickListener, delete)
            }
        }

    }


    class CodeViewHolder(
        val binding: CodesBinding
    ): RecyclerView.ViewHolder(binding.root) {


        fun bind(
            item: QRCodeEntity,
            clickListener: QRCodeListener,
            deleteClickListener: DeleteCodeListener
        ) {
            binding.clickListener = clickListener
            binding.imageClickListener = deleteClickListener
            binding.code = item
            binding.executePendingBindings()


        }

        companion object {
            fun from(parent: ViewGroup): CodeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CodesBinding.inflate(layoutInflater,parent,false)
                return CodeViewHolder(binding)
            }
        }
    }

}

class TrainingPlandDiffCallback : DiffUtil.ItemCallback<QRCodeEntity>() {
    override fun areItemsTheSame(oldItem: QRCodeEntity, newItem: QRCodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: QRCodeEntity, newItem: QRCodeEntity): Boolean {
        return oldItem == newItem
    }
}

class QRCodeListener(val clickListener: (id: Int) -> Unit){

    fun onClick(code: QRCodeEntity) = clickListener(code.id)
}


class DeleteCodeListener(val clickListener: (id: Int) -> Unit){

    fun onClick(code: QRCodeEntity) = clickListener(code.id)
}

