package com.sperek.pomodorotracker.application.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;

class ZonedDateTimeJsonDeserializer implements JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(
      JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString());
  }
}
