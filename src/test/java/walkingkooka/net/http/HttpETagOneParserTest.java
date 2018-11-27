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

public final class HttpETagOneParserTest extends HttpETagParserTestCase<HttpETagOneParser> {

    @Test
    public final void testSeparatorFails() {
        this.parseFails("\"ABC\",", ',');
    }

    @Test
    public final void testSeparatorWhitespaceFails() {
        this.parseFails("\"ABC\", ", ',');
    }

    @Test
    public final void testWeakSeparatorWhitespaceFails() {
        this.parseFails("W/\"ABC\", ", ',');
    }

    @Test
    public void testManyTags() {
        this.parseFails("\"A\",\"B\"", ',');
    }

    @Override
    HttpETag parseOne(final String text) {
        return HttpETagOneParser.parseOne(text);
    }

    @Override
    protected Class<HttpETagOneParser> type() {
        return HttpETagOneParser.class;
    }
}
