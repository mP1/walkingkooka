/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.build;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class MissingBuilderTest implements ClassTesting2<MissingBuilder>,
        BuilderTesting<MissingBuilder, String>,
        ThrowableTesting {

    // constants

    private final static String LABEL = "something";

    private final static String BEFORE = "before";

    // tests

    @Test
    public void testNothingMissing() {
        final MissingBuilder missing = MissingBuilder.empty();
        this.check(missing, "", 0, 0);
    }

    // add

    @Test
    public void testAddWithNullFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(NullPointerException.class, () -> missing.add(null));
    }

    @Test
    public void testAddWithEmptyFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.add(""));
    }

    @Test
    public void testAddWithWhitespaceFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.add(" \t"));
    }

    @Test
    public void testAdd() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.add(LABEL);
        this.check(missing, LABEL, 1, 1);
    }

    @Test
    public void testManyErrors() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.add("a");
        missing.add("b");
        missing.add("c");
        this.check(missing, "a, b, c", 3, 3);
    }

    // addIfNull

    @Test
    public void testAddIfNullWithNullLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(NullPointerException.class, () -> missing.addIfNull(false, null));
    }

    @Test
    public void testAddIfNullWithEmptyLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfNull(false, ""));
    }

    @Test
    public void testAddIfNullWithWhitespaceLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfNull(false, " \t"));
    }

    @Test
    public void testAddIfNullErrorWithNullValue() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfNull(null, LABEL);
        this.check(missing, LABEL, 1, 1);
    }

    @Test
    public void testAddIfNullError() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfNull(new Object(), "A");
        this.check(missing, "", 1, 0);
    }

    // addIfZero

    @Test
    public void testAddIfZeroWithNullLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(NullPointerException.class, () -> missing.addIfZero(1, null));
    }

    @Test
    public void testAddIfZeroWithEmptyLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfZero(1, ""));
    }

    @Test
    public void testAddIfZeroWithWhitespaceLabelFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfZero(1, " \t"));
    }

    @Test
    public void testAddIfZeroWithZero() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfZero(0, LABEL);
        this.check(missing, LABEL, 1, 1);
    }

    @Test
    public void testAddIfZeroWithNotZero() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfZero(1, "A");
        this.check(missing, "", 1, 0);
    }

    // addIfFalse

    @Test
    public void testAddIfFalseWithNullFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(NullPointerException.class, () -> missing.addIfFalse(false, null));
    }

    @Test
    public void testAddIfFalseWithEmptyFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfFalse(false, ""));
    }

    @Test
    public void testAddIfFalseWithWhitespaceFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.addIfFalse(false, " \t"));
    }

    @Test
    public void testAddIfFalseWithFalse() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfFalse(false, LABEL);
        this.check(missing, LABEL, 1, 1);
    }

    @Test
    public void testAddIfFalseWithTrue() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.addIfFalse(true, "A");
        this.check(missing, "", 1, 0);
    }

    @Test
    public void testFailIfMissingWithLabelNullFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(NullPointerException.class, () -> missing.failIfMissing(null));
    }

    @Test
    public void testFailIfMissingWithLabelEmptyFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.failIfMissing(""));
    }

    @Test
    public void testFailIfMissingWithLabelWhitespaceFails() {
        final MissingBuilder missing = MissingBuilder.empty();
        assertThrows(IllegalArgumentException.class, () -> missing.failIfMissing(" \t"));
    }

    @Test
    public void testFailIfMissingWhenNone() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.failIfMissing(BEFORE);
    }

    @Test
    public void testFailIfMissingWhenMany() {
        final MissingBuilder missing = MissingBuilder.empty();
        missing.add("1");
        missing.add("2");

        final BuilderException expected = assertThrows(BuilderException.class, () -> missing.failIfMissing(BEFORE));
        checkMessage(expected, BEFORE + " 1, 2");
    }

    private void check(final MissingBuilder missing, final String message, final int total,
                       final int missingCount) {
        this.buildAndCheck(missing, message);
        assertEquals(total, missing.total(), "total");
        assertEquals(missingCount, missing.missing(), "missing");
    }

    @Override
    public MissingBuilder createBuilder() {
        return MissingBuilder.empty();
    }

    @Override
    public Class<String> builderProductType() {
        return String.class;
    }

    @Override
    public Class<MissingBuilder> type() {
        return MissingBuilder.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
