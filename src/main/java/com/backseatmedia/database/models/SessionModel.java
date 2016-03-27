package com.backseatmedia.database.models;

import com.backseatmedia.database.enums.TabletActions;
import com.backseatmedia.database.models.media.MovieModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Data
public class SessionModel {
    @Expose PassengerModel passenger;
    @Expose TabletModel tablet;
    @Expose JourneyModel journey;
    @Expose List<Command> commands;
    @Expose List<MovieModel> movies;


    public SessionModel() {
        commands = new ArrayList<Command>();
    }


    /**
     * This is a helper function to add a command into the commands list!
     *
     * @see Command
     */
    public void addCommand(TabletActions opcode, String json) {
        Command command = new Command();
        command.setJson(json);
        command.setOpcode(opcode.toString());
        commands.add(command);
    }


    /**
     * This class represents a commands. Commands are instructions sent to the tablet to execute a
     * certain action. Actions can be anything not relating to the data in the session. Like
     * updating the Movies list, Showing a notification, Taking a photo etc..
     * <p/>
     * Each command has an opcode which identifies the command, and a JSON string which provides
     * extra data for that command.
     */
    public class Command {
        @Expose @Getter @Setter String opcode;
        @Expose @Getter @Setter String json;
    }
}