package de.leximon.quartz.mixin.classes.miscellaneous;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Suggest custom items and blocks
 */
@Mixin(CommandManager.class)
public class CommandManagerMixin {

    private static final SuggestionProvider<ServerCommandSource> SUGGESTIONS_BLOCK = (context, builder) -> {
        StringReader stringReader = new StringReader(builder.getInput());
        stringReader.setCursor(builder.getStart());
        BlockArgumentParser blockArgumentParser = new BlockArgumentParser(stringReader, false);
        try {
            blockArgumentParser.parse(true);
        } catch (CommandSyntaxException ignore) {
        }
        return blockArgumentParser.getSuggestions(builder, BlockTags.getTagGroup());
    };
    private static final SuggestionProvider<ServerCommandSource> SUGGESTIONS_ITEM = (context, builder) -> {
        StringReader stringReader = new StringReader(builder.getInput());
        stringReader.setCursor(builder.getStart());
        ItemStringReader itemStringReader = new ItemStringReader(stringReader, false);
        try {
            itemStringReader.consume();
        } catch (CommandSyntaxException ignore) {
        }
        return itemStringReader.getSuggestions(builder, ItemTags.getTagGroup());
    };

    @Inject(method = "argument", at = @At("RETURN"))
    private static <T> void inject(String name, ArgumentType<T> type, CallbackInfoReturnable<RequiredArgumentBuilder<ServerCommandSource, T>> cir) {
        if(type instanceof BlockStateArgumentType)
            cir.getReturnValue().suggests(SUGGESTIONS_BLOCK);
        else if(type instanceof ItemStackArgumentType)
            cir.getReturnValue().suggests(SUGGESTIONS_ITEM);
    }

}
