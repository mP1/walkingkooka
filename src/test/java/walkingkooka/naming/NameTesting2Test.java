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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.text.CharSequences;

public final class NameTesting2Test implements PublicClassTesting<NameTesting2<?, ?>> {

    // subtract.........................................................................................................

    @Test
    public void testRfc2045ConstantMissingSpace() {
        this.checkEquals(
            -1,
            NameTesting2.RFC2045.indexOf(' '),
            () -> CharSequences.quoteAndEscape(
                NameTesting2.RFC2045
            ).toString()
        );
    }

    @Test
    public void testSubtract() {
        this.subtractAndCheck(
            "AB",
            "B",
            "A"
        );
    }

    @Test
    public void testSubtractWithRepeatedCharacter() {
        this.subtractAndCheck(
            "ABB",
            "B",
            "A"
        );
    }

    private void subtractAndCheck(final String text,
                                  final String subtract,
                                  final String expected) {
        this.checkEquals(
            expected,
            NameTesting2.subtract(
                text,
                subtract
            ),
            () -> CharSequences.quoteAndEscape(text) + " subtract " + CharSequences.quoteAndEscape(subtract)
        );
    }

    // class............................................................................................................

    @Override
    public Class<NameTesting2<?, ?>> type() {
        return Cast.to(NameTesting2.class);
    }
}
