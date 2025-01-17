package response;


import exception.ErrorCase;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .data(data)
                .build();
    }

    public static CommonResponse<Object> success() {
        return CommonResponse.builder()
                .message("success")
                .build();
    }

    public static <T> CommonResponse<T> error(ErrorCase errorCase) {
        return CommonResponse.<T>builder()
            .status(errorCase.getHttpStatus())
            .message(errorCase.getMessage())
            .build();
    }

    public static <T> CommonResponse<T> error(HttpStatus httpStatus, String message) {
        return CommonResponse.<T>builder()
                .status(httpStatus)
                .message(message)
                .build();
    }

}
