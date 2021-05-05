package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command
{
    public String operation;
    public List<String> parameters;

    @Override
    public String toString() {
        return operation + ":" + String.join(",", parameters);
    }
}

