package com.project.EhrRoute.Utilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil
{
    private final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public String createJson(Object object) {

        ObjectMapper objectMapper = new ObjectMapper();

        String json = "";

        try {
            json = objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException Ex) {
            logger.error(Ex.getMessage());
        }

        return json;
    }
}
