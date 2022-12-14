package me.marquez.bettercinematics.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FileUtils {

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static <T> T loadFromJson(@NonNull File file, @NonNull Class<? extends T> clazz) throws IOException {
        return gson.fromJson(JsonParser.parseReader(new FileReader(file)), clazz);
    }

    public static <T> CompletableFuture<T> loadFromJsonAsync(@NonNull File file, @NonNull Class<? extends T> clazz) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                future.complete(gson.fromJson(JsonParser.parseReader(new FileReader(file)), clazz));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public static <T> CompletableFuture<Void> saveToJsonAsync(@NonNull File file, @NonNull Class<? extends T> clazz, @NonNull Object object) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                if(!file.exists()) {
                    File parentFile = file.getParentFile();
                    if(parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) throw new IOException("Can not make directories");
                    if(!file.createNewFile()) throw new IOException("Can not create new file");
                }
                FileWriter clearWriter = new FileWriter(file);
                clearWriter.write("");
                clearWriter.close();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                gson.toJson(object, clazz, writer);
                writer.flush();
                writer.close();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

}
