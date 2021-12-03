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
import walkingkooka.Cast;

public final class BuilderTestingTest implements BuilderTesting<Builder<String>, String> {

    @Test
    public void testBuildAndCheckPass() {
        final String product = "abc123";
        this.buildAndCheck(this.builder(product), product);
    }

    @Test
    public void testBuildAndCheckFails() {
        boolean failed = false;
        try {
            this.buildAndCheck(this.builder("different!"), "expected!");
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testBuildFailsPass() {
        this.buildFails(this.builderBuildThrows("throws!"));
    }

    @Test
    public void testBuildFailsWithMessagePass() {
        final String message = "throws!";
        this.buildFails(this.builderBuildThrows(message), message);
    }

    @Test
    public void testBuildFailsDoesntThrowFails() {
        boolean failed = false;
        try {
            this.buildFails(this.builder("1234"));
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testBuildFailsDifferentMessageFails() {
        boolean failed = false;
        try {
            this.buildFails(this.builderBuildThrows("different!"), "expected!");
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testBuildMissingFailsWithoutThrow() {
        boolean failed = false;
        try {
            this.buildMissingFails(this.builder("1"), "789");
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testBuildMissingFailsWithMessagePass() {
        this.buildMissingFails(this.builderBuildThrows("throws 123 456"), "123", "456");
    }

    @Test
    public void testBuildMissingFailsMissingMessage() {
        boolean failed = false;
        try {
            this.buildMissingFails(this.builderBuildThrows("throws! 123 456"), "789");
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    @Test
    public void testBuildMissingFailsMissingMessage2() {
        boolean failed = false;
        try {
            this.buildMissingFails(this.builderBuildThrows("throws! 123 456"), "123", "789");
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    private Builder<String> builder(final String product) {
        return () -> product;
    }

    private Builder<String> builderBuildThrows(final String message) {
        return () -> {
            throw new BuilderException(message);
        };
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public Builder<String> createBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<String> builderProductType() {
        return String.class;
    }

    @Override
    public Class<Builder<String>> type() {
        return Cast.to(Builder.class);
    }
}
