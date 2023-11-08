package payselection.demo.models

data class Card(
    val number: String,
    val date: String,
    val cvv: String? = null
)