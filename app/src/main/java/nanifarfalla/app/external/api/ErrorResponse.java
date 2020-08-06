package nanifarfalla.app.external.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Entidad para recibir respuestas de error de la API
 */
public class ErrorResponse {
    @SerializedName("message")
    String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public static ErrorResponse fromErrorBody(ResponseBody errorBody) {
        try {
            ErrorResponse errorResponse = new Gson()
                    .fromJson(errorBody.string(), ErrorResponse.class);

            return errorResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ErrorResponse();
    }
}
