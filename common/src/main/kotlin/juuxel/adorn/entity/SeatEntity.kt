package juuxel.adorn.entity

import juuxel.adorn.block.SeatBlock
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.getBlockPos
import juuxel.adorn.util.putBlockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SeatEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    init {
        noClip = true
        isInvulnerable = true
    }

    private var seatPos = BlockPos.ofFloored(this.pos)

    fun setPos(pos: BlockPos, blockOffset: Double) {
        check(!world.isClient) {
            "setPos must be called on the logical server"
        }
        updatePosition(pos.x + 0.5, pos.y + 0.25 + blockOffset, pos.z + 0.5)
        seatPos = pos
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        player.startRiding(this)
        return ActionResult.SUCCESS
    }

    override fun removePassenger(entity: Entity?) {
        super.removePassenger(entity)
        kill()
    }

    override fun kill() {
        removeAllPassengers()
        if (!world.isClient) {
            PlatformBridges.network.sendToTracking(this, EntityPassengersSetS2CPacket(this))
        }
        super.kill()
        val state = world.getBlockState(seatPos)
        if (state.block is SeatBlock) {
            world.setBlockState(seatPos, state.with(SeatBlock.OCCUPIED, false))
        }
    }

    override fun getMountedHeightOffset() = 0.0
    override fun createSpawnPacket() = PlatformBridges.network.createEntitySpawnPacket(this)
    override fun hasNoGravity() = true
    override fun isInvisible() = true

    override fun initDataTracker() {}

    override fun readCustomDataFromNbt(tag: NbtCompound) {
        seatPos = tag.getBlockPos("SeatPos")
    }

    override fun writeCustomDataToNbt(tag: NbtCompound) {
        tag.putBlockPos("SeatPos", seatPos)
    }
}
