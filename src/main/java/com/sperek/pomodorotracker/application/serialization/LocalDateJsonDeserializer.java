package com.sperek.pomodorotracker.application.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.LocalDate;

class LocalDateJsonDeserializer implements JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
  }
}
