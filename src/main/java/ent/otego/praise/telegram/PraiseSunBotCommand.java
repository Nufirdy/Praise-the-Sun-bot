package ent.otego.praise.telegram;

public enum PraiseSunBotCommand {
    START("start", "Alias for /location"),
    LOCATION("location", "Set your location to calculate sun position. Command accepts "
            + "arguments in following format: "
            + "/location 17.945000 -90.166139 where 17.945000 is latitude and "
            + "-90.166139 is longitude. Or send location in next message"),
    STOP("stop", "Unsubscribe from daily Praise the sun mailing");


    private final String commandLiteral;
    private final String description;

    PraiseSunBotCommand(String commandLiteral, String description) {
        this.commandLiteral = commandLiteral;
        this.description = description;
    }

    public String getCommandLiteral() {
        return commandLiteral;
    }

    public String getDescription() {
        return description;
    }

    public static PraiseSunBotCommand from(String commandLiteral) {
        switch (commandLiteral) {
            case "start":
                return START;
            case "location":
                return LOCATION;
            case "stop":
                return STOP;
        }
        return null;
    }
}
