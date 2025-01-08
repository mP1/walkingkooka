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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

public final class LineEndingSensitivityTest implements ClassTesting<LineEndingSensitivity> {

    @Test
    public void testExactEqualsSame() {
        final String text = "abc\ndef\rghi\r\n";

        this.equalsAndCheck(
            LineEndingSensitivity.EXACT,
            text,
            text,
            true
        );
    }

    @Test
    public void testExactEqualsDifferentLineEndings() {
        this.equalsAndCheck(
            LineEndingSensitivity.EXACT,
            "abc\n",
            "abc\r",
            false
        );
    }


    @Test
    public void testAnyEqualsSame() {
        final String text = "abc\ndef\rghi\r\n";

        this.equalsAndCheck(
            LineEndingSensitivity.ANY,
            text,
            text,
            true
        );
    }

    @Test
    public void testExactAnyDifferentLineEndings() {
        this.equalsAndCheck(
            LineEndingSensitivity.ANY,
            "abc\n",
            "abc\r",
            true
        );
    }

    @Test
    public void testExactAnyDifferentTextBetweenLineEndings() {
        this.equalsAndCheck(
            LineEndingSensitivity.ANY,
            "abc\n",
            "abc123\n",
            false
        );
    }

    private void equalsAndCheck(final LineEndingSensitivity sensitivity,
                                final CharSequence left,
                                final CharSequence right,
                                final boolean expected) {
        this.checkEquals(
            expected,
            sensitivity.equals(left, right),
            () -> sensitivity + ".equals " + CharSequences.quoteAndEscape(left) + " " + CharSequences.quoteAndEscape(right)
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<LineEndingSensitivity> type() {
        return LineEndingSensitivity.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
