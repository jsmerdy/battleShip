package common;

import java.util.Arrays;

public class Command
{
    public String operation;
    public String[] parameters;

    @Override
    public String toString() {
        return operation + ":" + String.join(",", parameters);
    }
}

