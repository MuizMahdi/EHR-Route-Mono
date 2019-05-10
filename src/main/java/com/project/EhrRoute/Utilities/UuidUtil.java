package com.project.EhrRoute.Utilities;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
public class UuidUtil
{
    public String generateUUID()
    {
        return UUID.randomUUID().toString();
    }

    public boolean isValidUUID(String uuid)
    {
        if (uuid.isEmpty()) {
            return false;
        }

        try {
            UUID uuidFromString = UUID.fromString(uuid);
        }
        catch (IllegalArgumentException Ex) {
            return false;
        }

        return true;
    }
}
