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

package walkingkooka.tree.text;

public final class WordBreakTest extends EnumTextStylePropertyValueTestCase<WordBreak> {

    @Override
    TextStylePropertyName<WordBreak> textStylePropertyName() {
        return TextStylePropertyName.WORD_BREAK;
    }

    @Override
    WordBreak createTextStylePropertyValue() {
        return WordBreak.BREAK_WORD;
    }

    @Override
    public Class<WordBreak> type() {
        return WordBreak.class;
    }
}