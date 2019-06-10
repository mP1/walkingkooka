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

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.net.header.MediaType;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.nio.charset.Charset;
import java.util.Optional;

public final class UrlTest implements ClassTesting2<Url>,
        ParseStringTesting<Url> {

    @Override
    public void testParseEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseAbsoluteUrl() {
        final String text = "http://example.com";

        this.parseAndCheck(text, Url.parseAbsolute(text));
    }

    @Test
    public void testParseDataUrl() {
        final DataUrl url = Url.data(Optional.of(MediaType.TEXT_PLAIN), Binary.with("abc123".getBytes(Charset.defaultCharset())));

        this.parseAndCheck(url.value(), Url.parseData(url.value()));
    }

    @Test
    public void testParseRelativeUrl() {
        final String text = "/path123?query456";

        this.parseAndCheck(text, Url.parseRelative(text));
    }

    @Test
    public void testParseRelativeUrlEmpty() {
        final String text = "";

        this.parseAndCheck(text, Url.parseRelative(text));
    }

    @Override
    public Class<Url> type() {
        return Url.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public Url parse(final String text) {
        return Url.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

}
