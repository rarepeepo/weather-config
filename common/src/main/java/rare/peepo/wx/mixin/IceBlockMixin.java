package rare.peepo.wx.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IceBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * At cold temperatures, a water block freezes into ice. It will not melt again
 * however when the temperature increases again, so we add that here.
 * 
 * For details, see here:
 *  https://minecraft.wiki/w/Ice#Post-generation
 */
@Mixin(IceBlock.class)
public class IceBlockMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
        var biome = world.getBiome(pos).value();
        if (!biome.isCold(pos))
            melt(state, world, pos);
    }
    
    // This is just copied from IceBlock.melt
    void melt(BlockState state, World world, BlockPos pos) {
        if (world.getDimension().ultrawarm()) {
            world.removeBlock(pos, false);
        } else {
            var meltedState = Blocks.WATER.getDefaultState();
            world.setBlockState(pos, meltedState);
            world.updateNeighbor(pos, meltedState.getBlock(), pos);
        }
    }
}
