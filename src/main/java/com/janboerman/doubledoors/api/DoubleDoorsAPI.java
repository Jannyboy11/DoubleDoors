package com.janboerman.doubledoors.api;

import com.janboerman.doubledoors.event.FakeBlockRedstoneEvent;
import com.janboerman.doubledoors.event.FakePlayerInteractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/// The developer's api for DoubleDoors.
public class DoubleDoorsAPI {

    private DoubleDoorsAPI() {
    }

    /// Tests whether a BlockRedstoneEvent is fake.
    ///
    /// A BlockRedstoneEvent is fake if the block in question does not actually experience a change in redstone power
    /// in the game world, but the event was called by DoubleDoors for an adjacent door/trapdoor/fencegate for improved
    /// interop with other plugins.
    ///
    /// @param event the (possibly fake) BlockRedstoneEvent
    ///
    /// @return true if the event is fake, otherwise false
    public static boolean isFakeEvent(BlockRedstoneEvent event) {
        return event instanceof FakeBlockRedstoneEvent;
    }

    /// Tests whether a PlayerInteractEvent is fake.
    ///
    /// A PlayerInteractEvent is fake the clicked block isn't really clicked by a real player in the world, but the
    /// event was called by DobuleDoors for an adjacent door/trapdoor/fencegate for improved interop with other plugins.
    ///
    /// @param event the (possibly fake) PlayerInteractEvent
    ///
    /// @return true if the event is fake, otherwise false
    public static boolean isFakeEvent(PlayerInteractEvent event) {
        return event instanceof FakePlayerInteractEvent;
    }
}
