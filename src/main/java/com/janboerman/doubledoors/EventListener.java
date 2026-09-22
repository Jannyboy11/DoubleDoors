package com.janboerman.doubledoors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import static org.bukkit.block.BlockFace.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EventListener implements Listener {

    private static final BlockFace[] ADJACENT_DIRECTIONS = { NORTH, EAST, SOUTH, WEST, UP, DOWN };

    private final DoubleDoorsPlugin plugin;

    public EventListener(DoubleDoorsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDoorRedstone(BlockRedstoneEvent event) {
        if (event instanceof FakeBlockRedstoneEvent) return;

        Block source = event.getBlock();
        adjustNeighbouringBlocks(source, otherDoorBlock -> {
            int newPower = event.getNewCurrent();

            FakeBlockRedstoneEvent fakeEvent = new FakeBlockRedstoneEvent(otherDoorBlock, otherDoorBlock.getBlockPower(), newPower);
            plugin.getServer().getPluginManager().callEvent(fakeEvent);
            //event is not cancellable, so just set the new block power
            var otherBlockData = (Openable) otherDoorBlock.getBlockData();
            var sourceBlockData = (Openable) source.getBlockData();

            if (otherBlockData instanceof Powerable) {
                ((Powerable) otherBlockData).setPowered(newPower > 0);
            }

            otherBlockData.setOpen(!sourceBlockData.isOpen());
            otherDoorBlock.setBlockData(otherBlockData, false);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event instanceof FakePlayerInteractEvent) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block source = event.getClickedBlock();
            Material type = source.getType();
            switch (type) {
                case IRON_DOOR:
                case IRON_TRAPDOOR:
                    //there is no isHumanOpenable method/interface sadly
                    break;
                default:
                    adjustNeighbouringBlocks(source, otherDoorBlock -> {
                        FakePlayerInteractEvent fakeEvent = new FakePlayerInteractEvent(event.getPlayer(), Action.RIGHT_CLICK_BLOCK, event.getItem(), otherDoorBlock, event.getBlockFace());
                        plugin.getServer().getPluginManager().callEvent(fakeEvent);
                        if (fakeEvent.useInteractedBlock() != Event.Result.DENY) {
                            var otherBlockData = (Openable) otherDoorBlock.getBlockData();
                            var sourceBlockData = (Openable) source.getBlockData();

                            otherBlockData.setOpen(!sourceBlockData.isOpen());
                            otherDoorBlock.setBlockData(otherBlockData, false);
                        }
                    });
            }
        }
    }

    private void adjustNeighbouringBlocks(Block origin, Consumer<? super Block> action) {
        BlockData blockData = origin.getBlockData();
        if (blockData instanceof Openable && blockData instanceof Directional) {
            Set<Block> set = new HashSet<>();
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

    private static class FakeBlockRedstoneEvent extends BlockRedstoneEvent {
        public FakeBlockRedstoneEvent(Block block, int oldCurrent, int newCurrent) {
            super(block, oldCurrent, newCurrent);
        }

        public static HandlerList getHandlerList() {
            return BlockRedstoneEvent.getHandlerList();
        }
    }

    private static class FakePlayerInteractEvent extends PlayerInteractEvent {
        public FakePlayerInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
            super(who, action, item, clickedBlock, clickedFace);
        }

        public static HandlerList getHandlerList() {
            return PlayerInteractEvent.getHandlerList();
        }
    }

}
