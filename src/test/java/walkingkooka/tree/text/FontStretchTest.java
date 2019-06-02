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

package walkingkooka.tree.text;

public final class FontStretchTest extends EnumTextPropertyValueTestCase<FontStretch> {

    @Override
    TextStylePropertyName<FontStretch> textStylePropertyName() {
        return TextStylePropertyName.FONT_STRETCH;
    }

    @Override
    FontStretch value() {
        return FontStretch.NORMAL;
    }

    @Override
    public Class<FontStretch> type() {
        return FontStretch.class;
    }
}
