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

public final class TextDecorationStyleTest extends EnumTextStylePropertyValueTestCase<TextDecorationStyle> {

    @Override
    TextStylePropertyName<TextDecorationStyle> textStylePropertyName() {
        return TextStylePropertyName.TEXT_DECORATION_STYLE;
    }

    @Override
    TextDecorationStyle createTextStylePropertyValue() {
        return TextDecorationStyle.DASHED;
    }

    @Override
    public Class<TextDecorationStyle> type() {
        return TextDecorationStyle.class;
    }
}