package de.leximon.quartz.mixin.classes.entity;

import com.mojang.authlib.GameProfile;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.entity.PlayerUtil;
import de.leximon.quartz.api.event.inventory.InventoryCloseEvent;
import de.leximon.quartz.api.event.inventory.InventoryOpenEvent;
import de.leximon.quartz.api.inventory.HandledInventory;
import de.leximon.quartz.api.inventory.ScreenHandlerFactory;
import de.leximon.quartz.mixin.classes.miscellaneous.PlayerManagerAccessor;
import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerUtil {

    @Shadow public abstract OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory);

    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Shadow protected abstract void onScreenHandlerOpened(ScreenHandler screenHandler);

    @Shadow protected abstract void updateScores(ScoreboardCriterion criterion, int score);

    @Shadow protected abstract void incrementScreenHandlerSyncId();

    @Shadow private int screenHandlerSyncId;

    @Shadow @Final public ServerPlayerInteractionManager interactionManager;

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

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

    @Override
    public <I extends ScreenHandler> I openInventory(Component title, I screenHandler) {
        this.networkHandler.sendPacket(new OpenScreenS2CPacket(screenHandler.syncId, screenHandler.getType(), Quartz.adventure().toNative(title)));
        this.onScreenHandlerOpened(screenHandler);
        this.currentScreenHandler = screenHandler;
        return screenHandler;
    }

    @Override
    public int nextScreenHandlerSyncId() {
        incrementScreenHandlerSyncId();
        return screenHandlerSyncId;
    }

    @Override
    public void openInventory(HandledInventory inventory) {
        OptionalInt optionalInt = openHandledScreen(inventory);
        if(optionalInt.isPresent())
            HandledInventory.registerForEvents((ServerPlayerEntity) (Object) this, currentScreenHandler, inventory);

    }

    @Inject(method = "closeScreenHandler", at = @At("HEAD"))
    private void injectOnInventoryClose(CallbackInfo ci) {
        Quartz.callEvent(new InventoryCloseEvent((ServerPlayerEntity) (Object) this, currentScreenHandler));
    }

    @Inject(method = "onScreenHandlerOpened", at = @At("HEAD"))
    private void injectOnInventoryOpen(ScreenHandler screenHandler, CallbackInfo ci) {
        Quartz.callEvent(new InventoryOpenEvent((ServerPlayerEntity) (Object) this, screenHandler));
    }

    @Override
    public GameMode getGameMode() {
        return interactionManager.getGameMode();
    }

    @Override
    public boolean setGameMode(GameMode gameMode) {
        return changeGameMode(gameMode);
    }
}
