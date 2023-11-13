package payselection.demo.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import payselection.demo.databinding.ICardBinding
import payselection.demo.models.UiCard
import payselection.demo.ui.checkout.common.CardListener


class CardAdapter(private val cardListener: CardListener) : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var cards: List<UiCard> = emptyList()

    inner class CardHolder(private val view: ICardBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                cardListener.onSelect(adapterPosition)
            }
        }

        fun bind(card: UiCard) {
            with(view) {
                cardNumber.text = card.title
                cardNumber.setTextColor(ContextCompat.getColor(root.context, card.textColor))
                if (card.cardType != null) {
                    imageCardType.setImageResource(card.cardType)
                } else {
                    imageCardType.setImageDrawable(null)
                }
                imageAdd.setImageResource(card.icon)
                root.setBackgroundResource(card.backGround)
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
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size

    fun updateData(listItem: List<UiCard>) {
        cards = listItem
        notifyDataSetChanged()
    }
}