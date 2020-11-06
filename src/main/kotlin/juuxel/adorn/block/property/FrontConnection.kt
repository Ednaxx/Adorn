package juuxel.adorn.block.property

import net.minecraft.util.StringIdentifiable

/**
 * Note: sofas have left and right flipped
 */
enum class FrontConnection(private val value: String) : StringIdentifiable {
    /**
     * No connection to the block in front.
     */
    NONE("none"),

    /**
     * The block will be connected to the block in front on its left side (looking from the connecting block).
     */
    LEFT("left"),

    /**
     * The block will be connected to the block in front on its right side (looking from the connecting block).
     */
    RIGHT("right");

    override fun asString() = value
}
