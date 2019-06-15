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

import walkingkooka.Cast;
import walkingkooka.test.Testing;
import walkingkooka.tree.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface HasTextTesting extends Testing {

    default void textAndCheck(final HasText has,
                              final String text) {
        assertEquals(text,
                has.text(),
                () -> (has instanceof HasText ? HasText.class.cast(has).text() : has.toString()) + (has instanceof Node ? "\n" + Node.class.cast(has).root() : ""));

        if (has instanceof HasTextLength) {
            final HasTextLength hasTextLength = Cast.to(has);
            assertEquals(text.length(),
                    hasTextLength.textLength(),
                    () -> (has.toString()) + (has instanceof Node ? "\n" + Node.class.cast(has).root() : ""));
        }
    }
}
