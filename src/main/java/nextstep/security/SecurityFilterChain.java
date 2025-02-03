package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SecurityFilterChain {
    List<Filter> getFilters();

    boolean matches(HttpServletRequest request);
}
