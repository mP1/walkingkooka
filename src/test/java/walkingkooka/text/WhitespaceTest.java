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

package walkingkooka.text;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import static org.junit.Assert.assertSame;

final public class WhitespaceTest extends HashCodeEqualsDefinedTestCase<Whitespace> {

    @Test
    public void testNullFails() {
        this.withFails(null);
    }

    @Test
    public void testIncludesNonWhitespaceCharacterFails() {
        this.withFails(" !");
    }

    private void withFails(final String content) {
        try {
            Whitespace.with(content);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testEmptyString() {
        assertSame(Whitespace.EMPTY, Whitespace.with(""));
    }

    @Test
    public void testWith() {
        final String content = "   ";
        final Whitespace whitespace = Whitespace.with(content);
        assertSame("toString", content, whitespace.toString());
    }

    @Override
    protected Class<Whitespace> type() {
        return Whitespace.class;
    }
}
