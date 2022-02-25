package de.leximon.quartz.api.event.player;

import com.mojang.authlib.GameProfile;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.Event;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minecraft.text.Text;

import java.net.SocketAddress;

@Getter
public class PlayerLoginEvent extends Event {

    private final SocketAddress address;
    private final GameProfile profile;
    private Component reason; // null == login allowed

    public PlayerLoginEvent(SocketAddress address, GameProfile profile, Text reason) {
        this.address = address;
        this.profile = profile;
        this.reason = Quartz.adventure().toAdventure(reason);
    }

    public boolean isAllowed() {
        return this.reason == null;
    }

    public void allow() {
        this.reason = null;
    }

    public void disallow(Component reason) {
        this.reason = reason == null ? Component.empty() : reason;
    }
}
