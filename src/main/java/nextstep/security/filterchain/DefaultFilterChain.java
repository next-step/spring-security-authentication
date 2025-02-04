package nextstep.security.filterchain;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;

import java.util.List;

public class DefaultFilterChain implements SecurityFilterChain {

    private List<Filter> filters;

    public DefaultFilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public List<Filter> getFilters() {
        return List.of();
    }

    @Override
    public boolean supports(ServletRequest servletRequest) {
        return false;
    }
}
