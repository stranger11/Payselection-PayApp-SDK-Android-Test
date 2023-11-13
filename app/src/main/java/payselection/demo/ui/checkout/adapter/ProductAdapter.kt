package payselection.demo.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import payselection.demo.databinding.IProductBinding
import payselection.demo.models.Product

class ProductAdapter(private val items: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    inner class ProductHolder(private val view: IProductBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: Product) {
            with(view) {
                title.text = item.name
                disc.text = item.description
                price.text = item.price
                image.setImageResource(item.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(
            IProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}