package com.dicoding.habitapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.databinding.HabitItemBinding

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : PagedListAdapter<Habit, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {
    //TODO 8 : Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        //TODO 9 : Get data and bind them to ViewHolder
        val habit: Habit? = getItem(position)
        if (habit != null) {
            holder.bind(habit)
        }
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = HabitItemBinding.bind(itemView)

        lateinit var getHabit: Habit
        fun bind(habit: Habit) {
            binding.apply {
                getHabit = habit
                when (getHabit.priorityLevel.uppercase()) {
                    "HIGH" -> itemPriorityLevel.setImageResource(R.drawable.ic_priority_high)
                    "MEDIUM" -> itemPriorityLevel.setImageResource(R.drawable.ic_priority_medium)
                    else -> itemPriorityLevel.setImageResource(R.drawable.ic_priority_low)
                }
                itemTvTitle.text = habit.title
                itemTvStartTime.text = habit.startTime
                itemTvMinutes.text = habit.minutesFocus.toString()
                itemView.setOnClickListener {
                    onClick(habit)
                }
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }

    }
}