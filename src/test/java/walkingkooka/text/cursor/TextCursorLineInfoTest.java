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

package walkingkooka.text.cursor;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

public final class TextCursorLineInfoTest implements ClassTesting<TextCursorLineInfo> {

    // invalidCharacterException........................................................................................

    @Test
    public void testInvalidCharacterExceptionWhenEmpty() {
        final String text = "";

        this.invalidCharacterExceptionAndCheck(
            TextCursors.charSequence(text)
                .lineInfo()
        );
    }

    @Test
    public void testInvalidCharacterExceptionWhenStart() {
        final String text = "ABC";

        this.invalidCharacterExceptionAndCheck(
            TextCursors.charSequence(text)
                .lineInfo(),
            Optional.of(
                new InvalidCharacterException(
                    text,
                    0
                )
            )
        );
    }

    @Test
    public void testInvalidCharacterExceptionWhenLastChar() {
        final String text = "ABC";

        this.invalidCharacterExceptionAndCheck(
            TextCursors.charSequence(text)
                .next()
                .next()
                .lineInfo(),
            new InvalidCharacterException(
                text,
                2
            )
        );
    }

    @Test
    public void testInvalidCharacterExceptionWhenCursorEmpty() {
        final String text = "ABC";

        this.invalidCharacterExceptionAndCheck(
            TextCursors.charSequence(text)
                .end()
                .lineInfo(),
            Optional.empty()
        );
    }

    private void invalidCharacterExceptionAndCheck(final TextCursorLineInfo info) {
        this.invalidCharacterExceptionAndCheck(
            info,
            Optional.empty()
        );
    }

    private void invalidCharacterExceptionAndCheck(final TextCursorLineInfo info,
                                                   final InvalidCharacterException expected) {
        this.invalidCharacterExceptionAndCheck(
            info,
            Optional.of(expected)
        );
    }

    private void invalidCharacterExceptionAndCheck(final TextCursorLineInfo info,
                                                   final Optional<InvalidCharacterException> expected) {
        this.checkEquals(
            expected,
            info.invalidCharacterException()
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextCursorLineInfo> type() {
        return TextCursorLineInfo.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
