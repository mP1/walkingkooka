
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

public final class GlobPatternComponentTextLiteralTest extends GlobPatternComponentTestCase<GlobPatternComponentTextLiteral> {

    private final static String TEXT = "Hello123";

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
            GlobPatternComponentTextLiteral.with("Different456")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), TEXT);
    }

    @Override
    public GlobPatternComponentTextLiteral createObject() {
        return GlobPatternComponentTextLiteral.with(TEXT);
    }

    @Override
    public Class<GlobPatternComponentTextLiteral> type() {
        return GlobPatternComponentTextLiteral.class;
    }
}
