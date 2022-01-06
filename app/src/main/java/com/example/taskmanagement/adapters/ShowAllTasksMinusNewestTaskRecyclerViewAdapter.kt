package com.example.taskmanagement.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.data.entities.Task
import com.example.taskmanagement.databinding.RecyclerViewItemShowAllTasksBinding
import android.annotation.SuppressLint
import com.example.taskmanagement.databinding.RecyclerViewItemShowAllTasksMinusNewestTaskBinding


class ShowAllTasksMinusNewestTaskRecyclerViewAdapter :
    RecyclerView.Adapter<ShowAllTasksMinusNewestTaskRecyclerViewAdapter.ShowTasksViewHolder>() {

     class ShowTasksViewHolder(binding : RecyclerViewItemShowAllTasksMinusNewestTaskBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task,
            newItem: Task
        ): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(
            oldItem: Task,
            newItem: Task
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    var dataList: List<Task>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Task>) {
        dataList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShowAllTasksMinusNewestTaskRecyclerViewAdapter.ShowTasksViewHolder {
        val recyclerViewItemShowAllTasks : RecyclerViewItemShowAllTasksMinusNewestTaskBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.recycler_view_item_show_all_tasks_minus_newest_task,
                parent,
                false
            )
        return ShowTasksViewHolder(recyclerViewItemShowAllTasks )
    }

    override fun getItemCount(): Int {
//        return dataList.size
        return dataList.size
    }

    override fun onBindViewHolder(
        holder: ShowAllTasksMinusNewestTaskRecyclerViewAdapter.ShowTasksViewHolder,
        position: Int
    ) {

        val taskItem = dataList[position]
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.task_title).text =
                taskItem.taskTitle
            holder.itemView.findViewById<TextView>(R.id.short_description).text =
                taskItem.shortDescription

            setOnClickListener {
                onItemClickListener?.let {
                    it(taskItem)
                }
            }

        }
    }

    private var onItemClickListener: ((Task) -> Unit)? = null

    fun setOnItemClickListener(listener: (Task) -> Unit) {
        onItemClickListener = listener
    }
}