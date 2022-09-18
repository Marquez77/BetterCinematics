package me.marquez.bettercinematics.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FileUtils {

    private static final Gson gson = new Gson();

    public static <T> CompletableFuture<T> loadFromJson(@NonNull File file, @NonNull Class<? extends T> clazz) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                future.complete(gson.fromJson(JsonParser.parseReader(new FileReader(file)), clazz));
            } catch (FileNotFoundException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public static <T> CompletableFuture<Void> saveToJson(@NonNull File file, @NonNull Class<? extends T> clazz, @NonNull Object object) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                gson.toJson(object, clazz, new FileWriter(file, true));
                future.complete(null);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

}
