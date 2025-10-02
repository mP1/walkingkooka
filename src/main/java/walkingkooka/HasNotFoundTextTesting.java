package walkingkooka;

import walkingkooka.test.Testing;

public interface HasNotFoundTextTesting extends Testing {

    default void notFoundTextAndCheck(final HasNotFoundText has,
                                      final String expected) {
        this.checkEquals(
            expected,
            has.notFoundText()
        );
    }
}