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
import walkingkooka.reflect.PublicClassTesting;

public final class HasTextTest implements PublicClassTesting<HasText> {

    // textOrEmptyString................................................................................................

    @Test
    public void testTextOrEmptyStringWithNull() {
        this.textOrEmptyStringAndCheck(
            null,
            ""
        );
    }

    @Test
    public void testTextOrEmptyStringWithNonNull() {
        final String text = "Hello";

        this.textOrEmptyStringAndCheck(
            new HasText() {
                @Override
                public String text() {
                    return text;
                }
            },
            text
        );
    }

    @Test
    public void testTextOrEmptyStringWithNonNullReturnsNull() {
        this.textOrEmptyStringAndCheck(
            new HasText() {
                @Override
                public String text() {
                    return null;
                }
            },
            null
        );
    }

    private void textOrEmptyStringAndCheck(final HasText has,
                                           final String expected) {
        this.checkEquals(
            expected,
            HasText.textOrEmptyString(has)
        );
    }

    // class............................................................................................................

    @Override
    public Class<HasText> type() {
        return HasText.class;
    }
}
