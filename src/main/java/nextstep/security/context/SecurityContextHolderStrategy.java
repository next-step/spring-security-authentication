package nextstep.security.context;

import java.util.function.Supplier;

public interface SecurityContextHolderStrategy {

    void clearContext();

    SecurityContext getContext();

    Supplier<SecurityContext> getDeferredContext();

    void setContext(SecurityContext context);

    void setDeferredContext(Supplier<SecurityContext> deferredContext);

    SecurityContext createEmptyContext();

}
