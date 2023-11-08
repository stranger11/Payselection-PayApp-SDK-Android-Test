package payselection.demo.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import payselection.demo.models.Product
import com.squareup.picasso.Picasso
import payselection.demo.databinding.IProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    private var list: List<Product> = emptyList()

    inner class ProductHolder(private val view: IProductBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: Product) {
            with(view) {
                title.setText(item.name)
                disc.setText(item.description)
                price.setText(item.price.toString())
                Picasso.get().load(item.image).into(image)
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

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(listItem: List<Product>) {
        list = listItem
        notifyDataSetChanged()
    }
}