package com.riphah.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riphah.food.R
import com.riphah.food.databinding.FragmentReceiveBinding
import com.riphah.food.databinding.ItemRecyclerViewReceiversBinding
import com.riphah.food.model.ReceiverModel
import com.riphah.food.ui.authntication.SignupFragment
import java.security.AccessControlContext

class ReceiverAdapter(private val context: Context) :
    RecyclerView.Adapter<ReceiverAdapter.MyViewHolder>() {

    private var myReceiverList: ArrayList<ReceiverModel> = ArrayList()
    private var cardClickListener: CardClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewReceiversBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myReceiverList.size
    }

    fun updateData(receiverList: List<ReceiverModel>) {
        myReceiverList.clear()
        myReceiverList.addAll(receiverList)
        notifyDataSetChanged()
    }

    fun setCardClickListener(listener: CardClickListener) {
        cardClickListener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(myReceiverList[position])
    }

    inner class MyViewHolder(private val binding: ItemRecyclerViewReceiversBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(model: ReceiverModel) {
            binding.model = model
            binding.executePendingBindings()
            Glide
                .with(binding.root)
                .load(model.imageUrl.toString())
                .placeholder(R.drawable.outline_home_work_24)
                .centerCrop()
                .into(binding.ImageViewReceiver)

            binding.buttonDonateCardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    cardClickListener?.onItemClick(myReceiverList[position])
                }
            }
        }
    }

    interface CardClickListener {
        fun onItemClick(model: ReceiverModel)
    }
}


//class ReceiverAdapter(context: Context):
//    RecyclerView.Adapter<ReceiverAdapter.MyViewHolder>() {
//
//    var myReceiverList:ArrayList<ReceiverModel> =ArrayList<ReceiverModel>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val inflater=LayoutInflater.from(parent.context)
//        val binding=ItemRecyclerViewReceiversBinding.inflate(inflater,parent,false)
//        return MyViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return myReceiverList.size
//    }
//    fun updateData(receiverList:List<ReceiverModel>){
//        myReceiverList.clear()
//        myReceiverList.addAll(receiverList)
//        notifyDataSetChanged()
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bindView(myReceiverList.get(position))
//    }
//    open class MyViewHolder(val binding: ItemRecyclerViewReceiversBinding):RecyclerView.ViewHolder(binding.root){
//        fun bindView(model:ReceiverModel){
//            binding.model=model
//            binding.executePendingBindings()
//            binding.buttonDonateCardView.setOnClickListener()
//        }
//
//
//    }
//    interface CardClickListener{
//        fun onItemCLick(model: ReceiverModel)
//    }
//}
