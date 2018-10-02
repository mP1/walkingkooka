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
 *
 */

package walkingkooka.text.spreadsheetformat;

import org.junit.Test;
import walkingkooka.color.Color;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Optional;

public final class SpreadsheetFormattedTextEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<SpreadsheetFormattedText> {

    private final static Optional<Color> COLOR = Optional.of(Color.BLACK);
    private final static String TEXT = "1/1/2000";


    @Test
    public void testDifferentColor() {
        this.checkNotEquals(SpreadsheetFormattedText.with(Optional.of(Color.WHITE), TEXT));
    }

    @Test
    public void testDifferentColor2() {
        this.checkNotEquals(SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, TEXT));
    }

    @Test
    public void testDifferentText() {
        this.checkNotEquals(SpreadsheetFormattedText.with(COLOR, "different"));
    }

    @Override
    protected SpreadsheetFormattedText createObject() {
        return SpreadsheetFormattedText.with(COLOR, TEXT);
    }
}
