/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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

import org.junit.Assert;
import org.junit.Test;

final public class MissingBuilderTest extends BuilderTestCase<MissingBuilder, String> {
    // constants

    private final static String LABEL = "something";

    private final static String BEFORE = "before";

    // tests

    @Test
    public void testNothingMissing() {
        final MissingBuilder missing = MissingBuilder.create();
        this.check(missing, "", 0, 0);
    }

    // add

    @Test
    public void testAddWithNullFails() {
        this.addFails(null);
    }

    @Test
    public void testAddWithWhitespaceFails() {
        this.addFails(" \t");
    }

    private void addFails(final String label) {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.add(label);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testAdd() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.add(MissingBuilderTest.LABEL);
        this.check(missing, MissingBuilderTest.LABEL, 1, 1);
    }

    @Test
    public void testManyErrors() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.add("a");
        missing.add("b");
        missing.add("c");
        this.check(missing, "a, b, c", 3, 3);
    }

    // addIfNull

    @Test
    public void testAddIfNullWithNullFails() {
        this.addIfNullFails(null);
    }

    @Test
    public void testAddIfNullWithWhitespaceFails() {
        this.addIfNullFails(" \t");
    }

    private void addIfNullFails(final String label) {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.addIfNull(1, label);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testNullErrorWithNull() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfNull(null, MissingBuilderTest.LABEL);
        this.check(missing, MissingBuilderTest.LABEL, 1, 1);
    }

    @Test
    public void testNullErrorWithNotNull() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfNull(new Object(), "A");
        this.check(missing, "", 1, 0);
    }

    // addIfZero

    @Test
    public void testAddIfZeroWithNullFails() {
        this.addIfZeroFails(null);
    }

    @Test
    public void testAddIfZeroWithWhitespaceFails() {
        this.addIfZeroFails(" \t");
    }

    private void addIfZeroFails(final String label) {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.addIfZero(1, label);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testAddIfZeroWithZero() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfZero(0, MissingBuilderTest.LABEL);
        this.check(missing, MissingBuilderTest.LABEL, 1, 1);
    }

    @Test
    public void testAddIfZeroWithNotZero() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfZero(1, "A");
        this.check(missing, "", 1, 0);
    }

    // addIfFalse

    @Test
    public void testAddIfFalseWithNullFails() {
        this.addIfFails(null);
    }

    @Test
    public void testAddIfFalseWithWhitespaceFails() {
        this.addIfFails(" \t");
    }

    private void addIfFails(final String label) {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.addIfFalse(true, label);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testAddIfFalseWithFalse() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfFalse(false, MissingBuilderTest.LABEL);
        this.check(missing, MissingBuilderTest.LABEL, 1, 1);
    }

    @Test
    public void testAddIfFalseWithTrue() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.addIfFalse(true, "A");
        this.check(missing, "", 1, 0);
    }

    @Test
    public void testFailIfMissingWithNullFails() {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.failIfMissing(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testFailIfMissingWithWhitespaceFails() {
        final MissingBuilder missing = MissingBuilder.create();
        try {
            missing.failIfMissing(" \t");
            Assert.fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testFailIfMissingWhenNone() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.failIfMissing(MissingBuilderTest.BEFORE);
    }

    @Test
    public void testFailIfMissingWhenMany() {
        final MissingBuilder missing = MissingBuilder.create();
        missing.add("1");
        missing.add("2");
        try {
            missing.failIfMissing(MissingBuilderTest.BEFORE);
            Assert.fail();
        } catch (final BuilderException expected) {
            Assert.assertEquals("message",
                    MissingBuilderTest.BEFORE + " 1, 2",
                    expected.getMessage());
        }

    }

    private void check(final MissingBuilder missing, final String message, final int total,
                       final int missingCount) {
        Assert.assertEquals("message", message, missing.build());
        Assert.assertEquals("total", total, missing.total());
        Assert.assertEquals("missing", missingCount, missing.missing());
    }

    @Override
    protected MissingBuilder createBuilder() {
        return MissingBuilder.create();
    }

    @Override
    protected Class<String> builderProductType() {
        return String.class;
    }

    @Override
    protected Class type() {
        return MissingBuilder.class;
    }
}
