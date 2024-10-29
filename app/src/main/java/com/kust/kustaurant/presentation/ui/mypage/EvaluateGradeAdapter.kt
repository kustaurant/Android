package com.kust.kustaurant.presentation.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemMyEvaluateGradeBinding

class EvaluateGradeAdapter(private val grade : Double): RecyclerView.Adapter<EvaluateGradeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemMyEvaluateGradeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(grade: Double) {
            val stars = listOf(binding.ivGrade1, binding.ivGrade2, binding.ivGrade3, binding.ivGrade4, binding.ivGrade5)
            val fullStars = grade.toInt()
            val halfStar = (grade - fullStars >= 0.5)

            for (i in stars.indices) {
                when {
                    i < fullStars -> stars[i].setImageResource(R.drawable.ic_star_full)
                    i == fullStars && halfStar -> stars[i].setImageResource(R.drawable.ic_star_half)
                    else -> stars[i].setImageResource(R.drawable.ic_star_empty)
                }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvaluateGradeAdapter.ViewHolder {
        val binding = ItemMyEvaluateGradeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvaluateGradeAdapter.ViewHolder, position: Int) {
        holder.bind(grade)
    }

    override fun getItemCount(): Int = 1
}