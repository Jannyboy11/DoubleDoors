package com.janboerman.doubledoors.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.bukkit.block.BlockFace.*;
import static org.bukkit.block.BlockFace.DOWN;
import static org.bukkit.block.BlockFace.UP;
import static org.bukkit.block.BlockFace.WEST;

class SharedListenerLogic {

    // Use List.<BlockFace>of() instead of BlockFace[] because the jit compiler can trust the internal array as stable.
    private static final List<BlockFace> ADJACENT_DIRECTIONS = List.of(NORTH, EAST, SOUTH, WEST, UP, DOWN);

    private SharedListenerLogic() {
    }

    static void adjustNeighbouringBlocks(Block origin, Consumer<? super Block> action) {
        BlockData blockData = origin.getBlockData();
        if (blockData instanceof Openable && blockData instanceof Directional) {
            Set<Block> set = new LinkedHashSet<>();
            recurseBlocks(origin, set);
            set.remove(origin);
            for (Block needsUpdate : set) {
                action.accept(needsUpdate);
            }
        }
    }

    private static void recurseBlocks(Block source, Set<Block> accumulate) {
        BlockFace sourceFacing = ((Directional) source.getBlockData()).getFacing();
        Material sourceType = source.getType();

        for (BlockFace direction : ADJACENT_DIRECTIONS) {
            Block otherBlock = source.getRelative(direction);
            if (otherBlock.getType() == sourceType && accumulate.add(otherBlock)) {
                BlockFace targetFacing = ((Directional) otherBlock.getBlockData()).getFacing();
                if (sourceFacing == targetFacing) {
                    recurseBlocks(otherBlock, accumulate);
                }
            }
        }
    }
}
