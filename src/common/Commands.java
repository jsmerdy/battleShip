package common;

import java.util.ArrayList;
import java.util.Arrays;

public class Commands
{
    public static final String shipLocation = "ship_location";
    public static final String shipConfirm = "ship_confirm";
    public static final String shot = "shot";
    public static final String shotResult = "shot_result";
    public static final String state = "state";

    public static Command parse(String line) {
        line = line.trim();
        String[] inputArray = line.split(":");
        Command command = new Command();
        command.operation = inputArray[0];
        command.parameters = inputArray[1].split(",");
        return command;
    }
    public static Command create(String operation, String[] parameters, int value)
    {
        Command command = new Command();
        command.operation = operation;
        command.parameters = parameters;
        return command;
    }

    public static Command create(String operation, Object ...parameters) {
        Command command = new Command();
        command.operation = operation;
        ArrayList<String> paramList = new ArrayList<>();
        for(Object o:parameters)
        {
            paramList.add(o.toString());
        }
        command.parameters = paramList.toArray(new String[0]);
        return command;
    }
}
