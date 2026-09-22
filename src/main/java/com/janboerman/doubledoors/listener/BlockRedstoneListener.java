package com.janboerman.doubledoors.listener;

import com.janboerman.doubledoors.DoubleDoorsPlugin;
import com.janboerman.doubledoors.event.FakeBlockRedstoneEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import static com.janboerman.doubledoors.listener.SharedListenerLogic.adjustNeighbouringBlocks;

public class BlockRedstoneListener implements Listener {

    private final DoubleDoorsPlugin plugin;

    public BlockRedstoneListener(DoubleDoorsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRedstonePowerChange(BlockRedstoneEvent event) {
        if (event instanceof FakeBlockRedstoneEvent) return;

        Block sourceDoor = event.getBlock();
        int newPower = event.getNewCurrent();

        adjustNeighbouringBlocks(sourceDoor, otherDoorBlock -> {
            FakeBlockRedstoneEvent fakeEvent = new FakeBlockRedstoneEvent(otherDoorBlock, otherDoorBlock.getBlockPower(), newPower);
            plugin.getServer().getPluginManager().callEvent(fakeEvent);
            //event is not cancellable, so just set the new block power
            var otherBlockData = (Openable) otherDoorBlock.getBlockData();

            boolean otherDoorOpen = fakeEvent.getNewCurrent() > 0;
            otherBlockData.setOpen(otherDoorOpen);
            otherDoorBlock.setBlockData(otherBlockData, false);
        });
    }

}
