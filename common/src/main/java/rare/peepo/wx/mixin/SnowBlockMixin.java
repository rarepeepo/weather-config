package rare.peepo.wx.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * At cold temperatures, snow generates on random blocks. It will not melt again
 * however when the temperature increases again, so we add that here.
 * 
 * For details, see here:
 *  https://minecraft.wiki/w/Snow#Post-generation
 */
@Mixin(SnowBlock.class)
public class SnowBlockMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
        var biome = world.getBiome(pos).value();
        if (!biome.isCold(pos)) {
            Block.dropStacks(state, world, pos);
            world.removeBlock(pos, false);
        }
    }
}
