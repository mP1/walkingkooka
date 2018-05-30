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

final public class LineTest extends CharSequenceTestCase<Line> {
    @Test
    public void testWithNullTextFails() {
        this.withFails(null);
    }

    @Test
    public void testWithCrTextFails() {
        this.withFails("abc \r");
    }

    @Test
    public void testWithNlTextFails() {
        this.withFails("abc \n");
    }

    private void withFails(final String text) {
        try {
            Line.with(text);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testEmpty() {
        this.createAndCheck("");
    }

    @Test
    public void testNotEmpty() {
        this.createAndCheck("abc123");
    }

    @Test
    public void testWhitespace() {
        this.createAndCheck("abc\tdef  ghi");
    }

    private void createAndCheck(final String text) {
        final Line line = Line.with(text);
        Assert.assertEquals("text value", text, line.value());
    }

    @Override
    protected Line createCharSequence() {
        return Line.with("abc");
    }
}
