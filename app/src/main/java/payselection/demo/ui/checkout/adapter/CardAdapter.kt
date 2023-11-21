package payselection.demo.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import payselection.demo.R
import payselection.demo.databinding.ICardBinding
import payselection.demo.models.Card
import payselection.demo.ui.checkout.common.CardListener
import payselection.demo.utils.ADD_ITEM_INDEX
import payselection.demo.utils.getPaymentSystem


class CardAdapter(private val cardListener: CardListener) :
    RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var selectedIndex: Int? = null
    private var list: List<Card> = emptyList()

    inner class CardHolder(private val view: ICardBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.root.setOnClickListener {
                cardListener.onSelect(adapterPosition)
            }
        }

        fun bind(card: Card, isSelected: Boolean) {
            with(view) {
                cardNumber.text = root.context.getString(R.string.card_number_format, card.number.takeLast(4))
                bgCardType.setBackgroundResource(R.drawable.bg_card_type)
                cardNumber.textSize = 12F
                val paymentSystem = getPaymentSystem(card.number.filter { it.isDigit() })
                if (paymentSystem != null) {
                    imageCardType.setImageResource(paymentSystem.image)
                } else {
                    imageCardType.setImageDrawable(null)
                }
                imageAdd.setImageResource(R.drawable.ic_ready)
                if (isSelected) {
                    cardNumber.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                    root.setBackgroundResource(R.drawable.bg_select_card)
                } else {
                    cardNumber.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                    root.setBackgroundResource(R.drawable.bg_card)
                }
            }
        }

        fun bindAddCard(isSelected: Boolean) {
            with(view) {
                cardNumber.text = root.context.getString(R.string.card_adding)
                imageCardType.setImageDrawable(null)
                cardNumber.textSize = 10F
                bgCardType.setBackgroundResource(R.drawable.bg_card_type_add)
                cardNumber.setTextColor(ContextCompat.getColor(root.context, R.color.gray))
                if (isSelected) {
                    root.setBackgroundResource(R.drawable.bg_card_add_selected)
                    imageAdd.setImageResource(R.drawable.ic_ready_blue)
                } else {
                    root.setBackgroundResource(R.drawable.bg_card)
                    imageAdd.setImageResource(R.drawable.ic_plus)
                }
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
        if (position < list.size) {
            holder.bind(list[position], selectedIndex == position)
        } else {
            holder.bindAddCard(selectedIndex == ADD_ITEM_INDEX)
        }
    }

    override fun getItemCount() = list.size + 1

    fun updatePosition(index: Int?) {
        selectedIndex = index
        notifyDataSetChanged()
    }

    fun updateList(list: List<Card>) {
        this.list = list
        notifyDataSetChanged()
    }
}