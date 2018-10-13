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

package walkingkooka.net;

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;

public final class UrlTest extends PublicClassTestCase<Url> {
    
    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        Url.parse(null);
    }
    
    @Test
    public void testParseAbsoluteUrl() {
        final String text = "http://example.com";
        final Url url = Url.parse(text);
        final AbsoluteUrl absoluteUrl = Url.parseAbsolute(text);
        assertEquals(text, absoluteUrl, url);
    }

    @Test
    public void testParseRelativeUrl() {
        final String text = "/path123?query456";
        final Url url = Url.parse(text);
        final RelativeUrl relativeUrl = Url.parseRelative(text);
        assertEquals(text, relativeUrl, url);
    }

    @Test
    public void testParseRelativeUrlEmpty() {
        final String text = "";
        final Url url = Url.parse(text);
        final RelativeUrl relativeUrl = Url.parseRelative(text);
        assertEquals(text, relativeUrl, url);
    }
    
    @Override
    protected Class<Url> type() {
        return Url.class;
    }
}
