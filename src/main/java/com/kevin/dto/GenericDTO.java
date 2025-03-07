package com.kevin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericDTO<T> {
	private boolean success;
    private String message;
    private T data;

    public GenericDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public static <T> GenericDTO<T> success(String message) { // 請求成功 不需要帶data
        return new GenericDTO<>(true, message, null);
    }
    public static <T> GenericDTO<T> success(T data) { // 請求成功 不需要帶 message 需要帶data
        return new GenericDTO<>(true, "請求成功", data);
    }

    public static <T> GenericDTO<T> success(String message, T data) { // 請求失敗 需要帶data
        return new GenericDTO<>(true, message, data);
    }

    public static <T> GenericDTO<T> error(String message) {  // 請求失敗 不需要帶data
        return new GenericDTO<>(false, message, null);
    }

}
