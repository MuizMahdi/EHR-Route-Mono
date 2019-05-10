package com.project.EhrRoute.Utilities;
import org.springframework.stereotype.Component;

@Component
public class SimpleStringUtil
{
    public boolean isValidNumber(String str)
    {
        try {
            long num = Long.parseLong(str);
        }
        catch (NumberFormatException Ex) {
            return false;
        }

        return true;
    }
}
