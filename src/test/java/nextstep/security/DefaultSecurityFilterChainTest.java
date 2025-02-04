package nextstep.security;

import jakarta.servlet.Filter;
import nextstep.security.fixture.TestSimpleFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DefaultSecurityFilterChainTest {

    @Test
    @DisplayName("DefaultSecurityFilterChain 에 필터들이 관리된다")
    void manageFilter() {
        Filter firstFilter = new TestSimpleFilter();
        Filter secondFilter = new TestSimpleFilter();

        DefaultSecurityFilterChain defaultSecurityFilterChain = new DefaultSecurityFilterChain(Arrays.asList(firstFilter, secondFilter));

        assertThat(defaultSecurityFilterChain.getFilters())
                .containsExactly(firstFilter, secondFilter);
    }

    @Test
    @DisplayName("DefaultSecurityFilterChain 에 관리되는 필터들은 불변이다")
    void unmodifiedFilter() {
        Filter firstFilter = new TestSimpleFilter();
        List<Filter> filterList = new ArrayList<>(List.of(firstFilter));

        DefaultSecurityFilterChain defaultSecurityFilterChain = new DefaultSecurityFilterChain(filterList);

        filterList.add(new TestSimpleFilter());

        assertThat(defaultSecurityFilterChain.getFilters())
                .containsExactly(firstFilter);
    }

    @Test
    @DisplayName("DefaultSecurityFilterChain 의 filterChain 새로운 필터를 추가하면 예외를 발생한다")
    void addingNewFilterToDefaultSecurityFilterChainThrowsException() {
        DefaultSecurityFilterChain defaultSecurityFilterChain = new DefaultSecurityFilterChain(List.of(new TestSimpleFilter()));

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> defaultSecurityFilterChain.getFilters().add(new TestSimpleFilter()));
    }

    @Test
    @DisplayName("DefaultSecurityFilterChain 는 전체 요청에 대해서 통과한다")
    void allMatched() {
        DefaultSecurityFilterChain defaultSecurityFilterChain = new DefaultSecurityFilterChain(List.of(new TestSimpleFilter()));

        boolean result = defaultSecurityFilterChain.matches(new MockHttpServletRequest());

        assertThat(result).isTrue();
    }

}
