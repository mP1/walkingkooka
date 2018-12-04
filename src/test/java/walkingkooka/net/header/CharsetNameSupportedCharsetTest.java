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
import walkingkooka.Cast;

import java.nio.charset.Charset;

public final class CharsetNameSupportedCharsetTest extends CharsetNameTestCase<CharsetNameSupportedCharset> {

    @Test
    public final void testCharsetSupported() {
        this.matches(CharsetName.UTF_8,
                true);
    }

    @Test
    public final void testCharsetSupported2() {
        final Charset utf8 = Charset.forName("utf-8");
        final Charset unsupported = Charset.availableCharsets()
                .values()
                .stream()
                .filter(c -> !utf8.contains(c))
                .findFirst()
                .get();

        this.matches(CharsetName.with(utf8.name()),
                CharsetName.with(unsupported.name()),
                false);
    }

    @Test
    public final void testCharsetUnsupported() {
        this.matches(CharsetNameUnsupportedCharset.unsupportedCharset("x-unsupported"),
                false);
    }

    @Override
    CharsetNameSupportedCharset createCharsetName() {
        return Cast.to(CharsetName.UTF_8);
    }

    @Override
    String headerText() {
        return "utf-8";
    }

    @Override
    String charsetNameToString() {
        return "UTF-8";
    }

    @Override
    protected Class<CharsetNameSupportedCharset> type() {
        return CharsetNameSupportedCharset.class;
    }
}
