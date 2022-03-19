package de.leximon.quartz.api.scoreboard;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.mixin.implementations.IServerScoreboard;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.function.Supplier;

@Getter
public class StructuredScoreboard {

    private final ServerScoreboard scoreboard;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final ScoreboardObjective objective;

    public StructuredScoreboard(ServerScoreboard scoreboard, String name, Component displayName) {
        this.scoreboard = scoreboard;
        this.objective = scoreboard.addObjective(name, ScoreboardCriterion.DUMMY, Quartz.adventure().toNative(displayName), ScoreboardCriterion.RenderType.INTEGER);
    }

    public StructuredScoreboard(ServerScoreboard scoreboard, String name) {
        this(scoreboard, name, Component.text(name));
    }

    public StructureBuilder setStructure() {
        return new StructureBuilder();
    }

    public void setActive() {
        scoreboard.setObjectiveSlot(1, objective);
    }

    public void update() {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.updateText();
            scoreboard.getPlayerScore(line.getEntryName(), objective)
                    .setScore(lines.size() - i);
        }
    }

    public Line getLine(int index) {
        return lines.get(index);
    }

    public class StructureBuilder {

        private final ArrayList<Line> lines = new ArrayList<>();

        private StructureBuilder() {}

        @CheckReturnValue
        public StructureBuilder free() {
            return this.text(Component::empty);
        }

        @CheckReturnValue
        public StructureBuilder text(Component text) {
            return this.text(() -> text);
        }

        @CheckReturnValue
        public StructureBuilder text(String text) {
            return this.text(Component.text(text));
        }

        @CheckReturnValue
        public StructureBuilder text(Supplier<Component> supplier) {
            lines.add(new Line(supplier));
            return this;
        }

        public void update() {
            StructuredScoreboard.this.lines.clear();
            for (Line line : this.lines) {
                line.createTeam();
                StructuredScoreboard.this.lines.add(line);
            }
            StructuredScoreboard.this.update();
        }
    }

    @Getter
    public class Line {

        private final Supplier<Component> textSupplier;
        private int id;
        private String entryName;
        private Team team;

        private Line(Supplier<Component> textSupplier) {
            this.textSupplier = textSupplier;
        }

        private void createTeam() {
            this.id = ((IServerScoreboard) scoreboard).getLineId();
            this.entryName = Integer.toHexString(id).replaceAll("(.)", "§$1");
            this.team = scoreboard.addTeam("struct-" + id);
            scoreboard.addPlayerToTeam(entryName, team);
            ((IServerScoreboard) scoreboard).increaseLineId();
        }

        public void updateText() {
            team.setPrefix(Quartz.adventure().toNative(textSupplier.get()));
        }
    }
}
