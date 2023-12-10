package pl.ozodbek.datastorepractice.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.datastorepractice.data.UserData
import pl.ozodbek.datastorepractice.databinding.UserDataRowItemBinding
import pl.ozodbek.datastorepractice.util.onClick
import pl.ozodbek.datastorepractice.util.viewBinding

class UserDataAdapter :
    ListAdapter<UserData, UserDataAdapter.MyViewHolder>(ToDoDiffUtil()) {

    private var itemClickListener: ((UserData) -> Unit)? = null

    fun setItemClickListener(listener: (UserData) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(parent.viewBinding(UserDataRowItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoList = getItem(position)
        todoList?.let { holder.onBind(it, itemClickListener) }
    }


    class MyViewHolder(private val binding: UserDataRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(userData: UserData, clickListener: ((UserData) -> Unit)?) {
            binding.apply {

                userNameTextview.text = userData.userName
                phoneNumberTextview.text = userData.phoneNumber

                root.onClick {
                    clickListener?.invoke(userData)
                }
            }
        }
    }


    private class ToDoDiffUtil : DiffUtil.ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData) =
            oldItem.phoneNumber == newItem.phoneNumber

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData) =
            oldItem == newItem

    }
}


