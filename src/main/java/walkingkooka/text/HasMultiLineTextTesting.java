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

import walkingkooka.test.Testing;

public interface HasMultiLineTextTesting extends Testing {

    default void multiLineTextAndCheck(final HasMultiLineText has,
                                       final LineEnding lineEnding,
                                       final String expected) {
        this.multiLineTextAndCheck(
            has,
            lineEnding,
            MultiLineText.with(expected)
        );
    }

    default void multiLineTextAndCheck(final HasMultiLineText has,
                                       final LineEnding lineEnding,
                                       final MultiLineText expected) {
        this.multiLineTextAndCheck(
            has,
            new FakeTextContext() {
                @Override
                public LineEnding lineEnding() {
                    return lineEnding;
                }
            },
            expected
        );
    }

    default void multiLineTextAndCheck(final HasMultiLineText has,
                                       final TextContext context,
                                       final String expected) {
        this.multiLineTextAndCheck(
            has,
            context,
            MultiLineText.with(expected)
        );
    }

    default void multiLineTextAndCheck(final HasMultiLineText has,
                                       final TextContext context,
                                       final MultiLineText expected) {
        this.checkEquals(
            expected,
            has.multiLineText(context),
            has::toString
        );
    }
}
