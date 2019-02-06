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

package walkingkooka.text.cursor.parser.json;

import walkingkooka.test.Fake;

public final class FakeJsonNodeParserTokenVisitorTest extends JsonNodeParserTokenVisitorTestCase<FakeJsonNodeParserTokenVisitor> {

    @Override
    protected FakeJsonNodeParserTokenVisitor createParserTokenVisitor() {
        return new FakeJsonNodeParserTokenVisitor();
    }

    @Override
    protected String requiredNamePrefix() {
        return Fake.class.getSimpleName();
    }

    @Override
    public Class<FakeJsonNodeParserTokenVisitor> parserTokenVisitorType() {
        return FakeJsonNodeParserTokenVisitor.class;
    }
}
