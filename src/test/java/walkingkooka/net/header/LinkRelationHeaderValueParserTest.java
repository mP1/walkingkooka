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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

import java.util.List;

public final class LinkRelationHeaderValueParserTest extends HeaderValueParserTestCase<LinkRelationHeaderValueParser,
        List<LinkRelation<?>>> {

    @Test
    public void testTokenSeparatorFails() {
        this.parseInvalidCharacterFails(";");
    }

    @Test
    public void testKeyValueSeparatorFails() {
        this.parseInvalidCharacterFails("=");
    }

    @Test
    public void testMultiValueSeparatorFails() {
        this.parseInvalidCharacterFails(",");
    }

    @Test
    public void testWildcardFails() {
        this.parseInvalidCharacterFails("*");
    }

    @Test
    public void testSlashFails() {
        this.parseInvalidCharacterFails("/");
    }

    @Override
    public List<LinkRelation<?>> parse(final String text) {
        return LinkRelationHeaderValueParser.parseLinkRelationList(text);
    }

    @Override
    String valueLabel() {
        return "value";
    }

    @Override
    public Class<LinkRelationHeaderValueParser> type() {
        return LinkRelationHeaderValueParser.class;
    }
}
