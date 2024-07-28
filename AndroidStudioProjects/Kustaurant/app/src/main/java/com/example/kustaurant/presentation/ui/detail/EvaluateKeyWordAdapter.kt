package com.example.kustaurant.presentation.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemEvaluateKeywordBinding

class EvaluateKeyWordAdapter(val keyword : ArrayList<String>)  : RecyclerView.Adapter<EvaluateKeyWordAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemEvaluateKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvaluateKeyWordAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: EvaluateKeyWordAdapter.ViewHolder, position: Int) {
        holder.bind(keyword[position])
    }

    override fun getItemCount(): Int = keyword.size

}