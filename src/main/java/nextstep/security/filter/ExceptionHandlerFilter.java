package nextstep.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            createErrorResponse(e, response);
        }
    }

    private void createErrorResponse(Throwable e, HttpServletResponse response) throws IOException {
        if (e instanceof Exception) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (e instanceof AuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        if (e instanceof IllegalArgumentException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        ErrorResponse errorResponse = new ErrorResponse(response.getStatus(), e.getMessage());
        String errorResponseJson = errorResponse.toJson();
        response.getWriter().write(errorResponseJson);
    }

    public static class ErrorResponse {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        private final Integer code;
        private final String message;

        public ErrorResponse(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String toJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(this);
        }
    }
}
