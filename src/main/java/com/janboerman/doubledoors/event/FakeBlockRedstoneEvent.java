package com.janboerman.doubledoors.event;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockRedstoneEvent;

public class FakeBlockRedstoneEvent extends BlockRedstoneEvent {
    public FakeBlockRedstoneEvent(Block block, int oldCurrent, int newCurrent) {
        super(block, oldCurrent, newCurrent);
    }

    public static HandlerList getHandlerList() {
        return BlockRedstoneEvent.getHandlerList();
    }
}
