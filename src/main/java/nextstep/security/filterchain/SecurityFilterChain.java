package nextstep.security.filterchain;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;

import java.util.List;

public interface SecurityFilterChain {
    List<Filter> getFilters();

    boolean supports(ServletRequest servletRequest);
}
