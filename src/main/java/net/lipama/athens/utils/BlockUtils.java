package net.lipama.athens.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.shape.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

import net.lipama.jhell.result.*;
import net.lipama.athens.Athens;

import java.util.stream.Stream;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class BlockUtils {
    private static final MinecraftClient MC = Athens.MC;

    public static BlockState getState(BlockPos pos) {
        assert MC.world != null;
        return MC.world.getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static int getId(BlockPos pos) {
        return Block.getRawIdFromState(getState(pos));
    }

    public static String getName(BlockPos pos) {
        return getName(getBlock(pos));
    }

    public static String getName(Block block) {
        return Registries.BLOCK.getId(block).toString();
    }

    public static Block getBlockFromName(String name) {
        try {
            return Registries.BLOCK.get(new Identifier(name));
        } catch (InvalidIdentifierException e) {
            return Blocks.AIR;
        }
    }

    @SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
    public static Result<Block> getBlockFromNameOrID(String nameOrId) {
        return JResult.from(() -> {
            if (isInteger(nameOrId)) {
                BlockState state = Block.STATE_IDS.get(Integer.parseInt(nameOrId));
                assert state == null;
                return state.getBlock();
            }
            return Registries.BLOCK.getOrEmpty(new Identifier(nameOrId)).get();
        });
    }

    public static float getHardness(BlockPos pos) {
        return getState(pos).calcBlockBreakingDelta(MC.player, MC.world, pos);
    }

    private static VoxelShape getOutlineShape(BlockPos pos) {
        return getState(pos).getOutlineShape(MC.world, pos);
    }

    public static Box getBoundingBox(BlockPos pos) {
        return getOutlineShape(pos).getBoundingBox().offset(pos);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getOutlineShape(pos) != VoxelShapes.empty();
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<>();

        BlockPos min = new BlockPos(
            Math.min(from.getX(), to.getX()),
            Math.min(from.getY(), to.getY()),
            Math.min(from.getZ(), to.getZ())
        );
        BlockPos max = new BlockPos(
            Math.max(from.getX(), to.getX()),
            Math.max(from.getY(), to.getY()),
            Math.max(from.getZ(), to.getZ())
        );

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Result<Stream<BlockPos>> getAllInBoxStream(BlockPos from, BlockPos to) {
        return JResult.from(() -> {
            BlockPos min = new BlockPos(
                Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()),
                Math.min(from.getZ(), to.getZ())
            );
            BlockPos max = new BlockPos(
                Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()),
                Math.max(from.getZ(), to.getZ())
            );

            Stream<BlockPos> stream = Stream.iterate(min, pos -> {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                x++;
                if (x > max.getX()) {
                    x = min.getX();
                    y++;
                }
                if (y > max.getY()) {
                    y = min.getY();
                    z++;
                }
                if (z > max.getZ()) throw new IllegalStateException("Stream limit didn't work.");
                return new BlockPos(x, y, z);
            });

            int limit = (max.getX() - min.getX() + 1) * (max.getY() - min.getY() + 1) * (max.getZ() - min.getZ() + 1);

            return stream.limit(limit);
        });
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
