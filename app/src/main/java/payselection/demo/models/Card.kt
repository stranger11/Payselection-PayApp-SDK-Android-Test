package payselection.demo.models

data class Card(
    val number: String,
    val date: String,
    var cvv: String? = null
)