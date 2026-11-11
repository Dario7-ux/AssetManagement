package org.example.assets.infraestructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionResponse<T> {

    private boolean success;
    private String message;
    private T results;

    public static <T> ActionResponse<T> ok(String message, T results) {
        return ActionResponse.<T>builder()
                .success(true)
                .message(message)
                .results(results)
                .build();
    }

    public static <T> ActionResponse<T> error(String message) {
        return ActionResponse.<T>builder()
                .success(false)
                .message(message)
                .results(null)
                .build();
    }
}

