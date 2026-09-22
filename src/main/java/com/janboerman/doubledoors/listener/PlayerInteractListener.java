package com.janboerman.doubledoors.listener;

import com.janboerman.doubledoors.DoubleDoorsPlugin;
import com.janboerman.doubledoors.event.FakePlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.janboerman.doubledoors.listener.SharedListenerLogic.adjustNeighbouringBlocks;

public class PlayerInteractListener implements Listener {

    private final DoubleDoorsPlugin plugin;

    public PlayerInteractListener(DoubleDoorsPlugin plugin) {
        this.plugin = plugin;
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
                    BlockData sourceBlockData = source.getBlockData();
                    adjustNeighbouringBlocks(source, otherDoorBlock -> {
                        var openableSourceBlockData = (Openable) sourceBlockData;
                        FakePlayerInteractEvent fakeEvent = new FakePlayerInteractEvent(event.getPlayer(), Action.RIGHT_CLICK_BLOCK, event.getItem(), otherDoorBlock, event.getBlockFace());
                        plugin.getServer().getPluginManager().callEvent(fakeEvent);
                        if (fakeEvent.useInteractedBlock() != Event.Result.DENY) {
                            var otherBlockData = (Openable) otherDoorBlock.getBlockData();

                            otherBlockData.setOpen(!openableSourceBlockData.isOpen()); // note, currently sourceBlockData is still in the 'old' state, hence the negation.
                            otherDoorBlock.setBlockData(otherBlockData, false);
                        }
                    });
            }
        }
    }

}
