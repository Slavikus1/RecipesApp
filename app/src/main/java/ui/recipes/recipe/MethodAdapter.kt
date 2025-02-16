package ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(var dataset: List<String>) :
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
        var numberOfMethod = dataset.indexOf(method)
        numberOfMethod += 1
        val text = "$numberOfMethod. $method"
        holder.methodTextView.text = text
    }

    override fun getItemCount(): Int = dataset.size
}