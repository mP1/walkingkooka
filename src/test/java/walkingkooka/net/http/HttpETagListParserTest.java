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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.Assert.assertEquals;

public final class HttpETagListParserTest extends HttpETagParserTestCase<HttpETagListParser> {

    @Test
    public final void testSeparatorFails() {
        final String text = "\"ABC\",";
        this.parseFails(text, HttpETagParser.missingETagValue(text));
    }

    @Test
    public final void testSeparatorSpaceFails() {
        final String text = "\"ABC\", ";
        this.parseFails(text, HttpETagParser.missingETagValue(text));
    }

    @Test
    public final void testSeparatorTabFails() {
        final String text = "\"ABC\",\t";
        this.parseFails(text, HttpETagParser.missingETagValue(text));
    }

    @Test
    public final void testWeakSeparatorSpaceFails() {
        final String text = "W/\"ABC\", ";
        this.parseFails(text, HttpETagParser.missingETagValue(text));
    }

    @Test
    public final void testWeakSeparatorTabFails() {
        final String text = "W/\"ABC\",\t";
        this.parseFails(text, HttpETagParser.missingETagValue(text));
    }

    @Test
    public void testETagSeparatorETag() {
        this.parseAndCheck2("\"A\",\"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.STRONG));
    }

    @Test
    public void testETagSeparatorSpaceETag() {
        this.parseAndCheck2("\"A\", \"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.STRONG));
    }

    @Test
    public void testETagSeparatorTabETag() {
        this.parseAndCheck2("\"A\",\t\"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.STRONG));
    }

    @Test
    public void testETagSeparatorSpaceSpaceETag() {
        this.parseAndCheck2("\"A\",  \"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.STRONG));
    }

    @Test
    public void testWeakETagSeparatorWeakETag() {
        this.parseAndCheck2("W/\"A\",W/\"B\"",
                HttpETag.with("A", HttpETagValidator.WEAK),
                HttpETag.with("B", HttpETagValidator.WEAK));
    }

    @Test
    public void testWeakETagSeparatorSpaceWeakETag() {
        this.parseAndCheck2("\"A\", W/\"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.WEAK));
    }

    @Test
    public void testWeakETagSeparatorTabWeakETag() {
        this.parseAndCheck2("\"A\",\tW/\"B\"",
                HttpETag.with("A", HttpETagValidator.STRONG),
                HttpETag.with("B", HttpETagValidator.WEAK));
    }

    final void parseAndCheck2(final String text, final HttpETag... tags) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(text),
                Lists.of(tags),
                HttpETagListParser.parseList(text));
    }

    @Override
    HttpETag parseOne(final String text) {
        final List<HttpETag> tags = HttpETagListParser.parseList(text);
        assertEquals("expected one tag =" + CharSequences.quote(text) + "=" + tags, 1, tags.size());
        return tags.get(0);
    }

    @Override
    protected Class<HttpETagListParser> type() {
        return HttpETagListParser.class;
    }
}
