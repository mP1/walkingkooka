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

package walkingkooka.net.header;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.type.MemberVisibility;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class AcceptCharsetTest extends HeaderValue2TestCase<AcceptCharset, List<CharsetHeaderValue>>{

    // charset...................................................................................................

    @Test
    public void testCharset() {
        this.charsetAndCheck(this.createHeaderValue(), CharsetName.UTF_8.charset());
    }

    @Test
    public void testCharset2() {
        this.charsetAndCheck(
                this.createHeaderValue(CharsetHeaderValue.with(CharsetName.UTF_8)),
                CharsetName.UTF_8.charset());
    }

    @Test
    public void testCharsetWithout() {
        this.charsetAndCheck(
                this.createHeaderValue(CharsetHeaderValue.with(CharsetName.with("X-custom"))));
    }

    private void charsetAndCheck(final AcceptCharset acceptCharset, final Charset charset) {
        this.charsetAndCheck(acceptCharset, Optional.of(charset));
    }

    private void charsetAndCheck(final AcceptCharset acceptCharset) {
        this.charsetAndCheck(acceptCharset, Optional.empty());
    }

    private void charsetAndCheck(final AcceptCharset acceptCharset, final Optional<Charset> expected) {
        assertEquals(acceptCharset + " .charset()",
                expected,
                acceptCharset.charset());
    }

    // helpers.......................................................................................................

    @Override
    AcceptCharset createHeaderValue(final List<CharsetHeaderValue> value) {
        return AcceptCharset.with(value);
    }

    private AcceptCharset createHeaderValue(final CharsetHeaderValue...value) {
        return this.createHeaderValue(Lists.of(value));
    }

        @Override
    List<CharsetHeaderValue> value() {
        return Lists.of(CharsetHeaderValue.with(CharsetName.with("X-custom")),
                CharsetHeaderValue.with(CharsetName.UTF_8),
                CharsetHeaderValue.with(CharsetName.UTF_16));
    }

    @Override
    List<CharsetHeaderValue> differentValue() {
        return Lists.of(CharsetHeaderValue.with(CharsetName.UTF_16));
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return false;
    }

    @Override
    protected Class<AcceptCharset> type() {
        return AcceptCharset.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
