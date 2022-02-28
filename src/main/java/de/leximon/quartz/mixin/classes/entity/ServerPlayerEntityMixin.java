package de.leximon.quartz.mixin.classes.entity;

import com.mojang.authlib.GameProfile;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.entity.PlayerUtil;
import de.leximon.quartz.api.inventory.ScreenHandlerFactory;
import de.leximon.quartz.mixin.classes.miscellaneous.PlayerManagerAccessor;
import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.OptionalInt;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerUtil {

    @Shadow public abstract OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    private ServerScoreboard scoreboard;

    @Override
    public void setDisplayScoreboard(ServerScoreboard scoreboard) {
        final MinecraftServer server = Quartz.getServer();
        final PlayerManager playerManager = server.getPlayerManager();
        ((PlayerManagerAccessor) playerManager).quartzSendScoreboard(scoreboard, (ServerPlayerEntity) (Object) this);
        this.scoreboard = scoreboard;
    }

    @Override
    public ServerScoreboard getDisplayScoreboard() {
        return scoreboard;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends ScreenHandler> I openInventory(Component title, ScreenHandlerFactory<I> factory) {
        OptionalInt optionalInt = openHandledScreen(new SimpleNamedScreenHandlerFactory(factory::createMenu, Quartz.adventure().toNative(title)));
        return optionalInt.isEmpty() ? null : (I) currentScreenHandler;
    }
}
