package com.toolkitmc.core.api.event;

/**
 * Marker interface for events that can be cancelled.
 *
 * <p>Use with {@link TmEventBus#fireAndCheck(Cancellable)}:
 * <pre>
 *   public class MyBlockBreakEvent implements Cancellable {
 *       private boolean cancelled;
 *       private final BlockPos pos;
 *
 *       public MyBlockBreakEvent(BlockPos pos) { this.pos = pos; }
 *
 *       public BlockPos getPos() { return pos; }
 *
 *       {@literal @}Override public boolean isCancelled() { return cancelled; }
 *       {@literal @}Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
 *   }
 *
 *   boolean allowed = TmCore.events().fireAndCheck(new MyBlockBreakEvent(pos));
 * </pre>
 */
public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
