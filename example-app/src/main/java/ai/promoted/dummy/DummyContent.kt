package ai.promoted.dummy

import ai.promoted.R
import java.util.*

object DummyContent {

    private val names = listOf(
        "Ra-Ra Ramen",
        "Oodles of Noodles",
        "Not Your Instant Cup",
        "Posh Pork Belly",
        "Broth Bros."
    )
    
    private val prices = listOf(
        "$$$",
        "$$",
        "$$$",
        "$$$$",
        "$$"
    )
    
    private val ratings = listOf(
        "4.5",
        "4.2",
        "4.7",
        "4.9",
        "4.1"
    )
    
    private val images = listOf(
        R.drawable.ramen_1,
        R.drawable.ramen_2,
        R.drawable.ramen_3,
        R.drawable.ramen_4,
        R.drawable.ramen_5
    )

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        repeat(COUNT) {
            addItem(createDummyItem(it))
        }
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        val name = when(position) {
            in 0..names.lastIndex -> names[position]
            else -> "Restaurant #${position + 1}"
        }

        val price = when(position) {
            in 0..prices.lastIndex -> prices[position]
            in prices.size until COUNT -> prices[position % prices.size]
            else -> "$$"
        }

        val ratingNumberString = when(position) {
            in 0..ratings.lastIndex -> ratings[position]
            in ratings.size until COUNT -> ratings[position % ratings.size]
            else -> "5.0"
        }

        val image = when(position) {
            in 0..images.lastIndex -> images[position]
            in images.size until COUNT -> images[position % images.size]
            else -> R.drawable.welcome_bg
        }

        return DummyItem(
            id = "$position",
            name = name,
            price = price,
            rating = "\u2b50 $ratingNumberString",
            image = image
        )
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(
        val id: String,
        val name: String,
        val price: String,
        val rating: String,
        val image: Int
    )
}