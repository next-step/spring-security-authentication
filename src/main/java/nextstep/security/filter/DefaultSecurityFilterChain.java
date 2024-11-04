package nextstep.security.filter;

import java.util.List;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

public class DefaultSecurityFilterChain implements SecurityFilterChain {

    private List<Filter> filters;

    public DefaultSecurityFilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        // todo 구현 예정
        return true;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
