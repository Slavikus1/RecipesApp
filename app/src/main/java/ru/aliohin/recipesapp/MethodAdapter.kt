package ru.aliohin.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataset: List<String>) :
    RecyclerView.Adapter<MethodAdapter.MethodHolder>() {
    class MethodHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        var methodTextView = binding.tvMethodDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MethodHolder(binding)
    }

    override fun onBindViewHolder(holder: MethodHolder, position: Int) {
        val method = dataset [position]
        holder.methodTextView.text = method
    }

    override fun getItemCount(): Int = dataset.size
}