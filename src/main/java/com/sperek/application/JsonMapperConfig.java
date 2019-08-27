package com.sperek.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.plugin.json.JavalinJson;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class JsonMapperConfig implements Runnable{

  @Override
  public void run() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJsonDeserializer())
        .registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer())
        .create();
    JavalinJson.setFromJsonMapper(gson::fromJson);
    JavalinJson.setToJsonMapper(gson::toJson);
  }
}
