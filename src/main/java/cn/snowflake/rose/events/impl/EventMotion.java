package cn.snowflake.rose.events.impl;


import com.darkmagician6.eventapi.types.EventType;
import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class EventMotion implements Event, Cancellable
{
    public double y;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean cancel;
    public EventType type;

    public boolean isCancel() {
        return cancel;
    }
    public EventMotion(final double y, float yaw, float pitch) {
        super();
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.type = EventType.PRE;
    }
    public EventMotion(final double y, float yaw, float pitch, boolean onGround) {
        super();
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = EventType.PRE;
    }

    public EventType getEventType() {
        return this.type;
    }

    public EventMotion(final EventType type) {
        this.type = type;
    }

    public boolean isPre() {
        return this.type == EventType.PRE;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    public double getY() {
        return this.y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(final float yaw) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, Minecraft.getMinecraft().thePlayer.rotationPitch, Minecraft.getMinecraft().thePlayer.onGround));

        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(final float pitch) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.getMinecraft().thePlayer.rotationYaw, pitch, Minecraft.getMinecraft().thePlayer.onGround));

        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }

}

