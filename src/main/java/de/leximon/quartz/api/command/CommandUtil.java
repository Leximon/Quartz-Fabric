package de.leximon.quartz.api.command;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.ServerCommandSource;

import javax.annotation.CheckReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommandUtil {

    public static <T> SuggestionsBuilder<T> suggestionList(Collection<T> list, Function<T, String> valueProvider) {
        return new SuggestionsBuilder<>(list, valueProvider);
    }

    public static class SuggestionsBuilder<T> {
        private final Collection<T> list;
        private final Function<T, String> valueProvider;
        private Comparator<T> comparator;
        private Function<T, String> tooltipProvider;
        private BiPredicate<T, String> filter;
        public SuggestionsBuilder(Collection<T> list, Function<T, String> valueProvider) {
            this.list = list;
            this.valueProvider = valueProvider;
            this.filter = (v, input) -> valueProvider.apply(v).toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT));
        }

        @CheckReturnValue
        public SuggestionsBuilder<T> sortedByString() {
            this.comparator = Comparator.comparing(valueProvider);
            return this;
        }

        @CheckReturnValue
        public SuggestionsBuilder<T> sorted(Comparator<T> comparator) {
            this.comparator = comparator;
            return this;
        }

        @CheckReturnValue
        public SuggestionsBuilder<T> filter(BiPredicate<T, String> filter) {
            this.filter = filter;
            return this;
        }

        @CheckReturnValue
        public SuggestionsBuilder<T> tooltipProvider(Function<T, String> provider) {
            this.tooltipProvider = provider;
            return this;
        }

        public SuggestionProvider<ServerCommandSource> build() {
            return (context, builder) -> {
                String input = builder.getInput().substring(builder.getStart());
                Stream<T> stream = list.stream()
                        .filter(v -> filter.test(v, input));
                if(comparator != null)
                    stream = stream.sorted(comparator);
                stream.map(v -> new Suggestion(valueProvider.apply(v), tooltipProvider != null ? tooltipProvider.apply(v) : null))
                        .forEach(sug -> builder.suggest(sug.value, () -> sug.tooltip));
                return builder.buildFuture();
            };
        }

        private record Suggestion(String value, String tooltip) { }
    }

}