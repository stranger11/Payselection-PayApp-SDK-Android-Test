package payselection.demo.ui.checkout.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import payselection.demo.R
import payselection.demo.models.UiCard
import payselection.demo.databinding.ICardBinding
import payselection.demo.ui.checkout.common.CardListener


class CardAdapter(private val cardListener: CardListener, private val context: Context) : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var list: List<UiCard> = listOf(
        UiCard(
            title = context.getString(R.string.card_adding), cardType = null, R.drawable.ic_plus, R.drawable.bg_card,
            R.color.gray
        )
    )

    inner class CardHolder(private val view: ICardBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                cardListener.onSelect(adapterPosition)
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(card: UiCard) {
            with(view) {
                cardNumber.setText(card.title)
                if (card.cardType != null) {
                    imageCardType.setImageResource(card.cardType)
                } else {
                    imageCardType.setImageDrawable(null)
                }
                imageAdd.setImageResource(card.icon)
                view.root.setBackgroundResource(card.backGround)
                view.cardNumber.setTextColor(context.getColor(card.textColor))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        return CardHolder(
            ICardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun updateData(listItem: List<UiCard>) {
        list = listItem
        notifyDataSetChanged()
    }
}