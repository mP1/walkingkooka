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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;
import walkingkooka.text.CharSequences;

public final class JavaNameIdentifierEmulatedTest implements ClassTesting<JavaNameIdentifierEmulated> {

    @Test
    public void testStart() {
        final StringBuilder diff = new StringBuilder();

        for (int i = 0; i <= 99; i++) {
            final char c = (char) i;

            if (JavaNameIdentifier.isStart(c) != JavaNameIdentifierEmulated.isStart(c)) {
                diff.append(
                        CharSequences.quoteIfChars(c)
                );
            }
        }

        this.checkEquals(
                "",
                diff.toString()
        );
    }

    @Test
    public void testPart() {
        final StringBuilder diff = new StringBuilder();

        for (int i = 0; i <= Character.MAX_VALUE; i++) {
            final char c = (char) i;

            if (JavaNameIdentifier.isPart(c) != JavaNameIdentifierEmulated.isPart(c)) {
                diff.append(
                        CharSequences.quoteIfChars(c)
                );
            }
        }

        this.checkEquals(
                "",
                diff.toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<JavaNameIdentifierEmulated> type() {
        return JavaNameIdentifierEmulated.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
