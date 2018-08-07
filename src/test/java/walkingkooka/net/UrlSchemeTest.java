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
import walkingkooka.naming.NameTestCase;

public final class UrlSchemeTest extends NameTestCase<UrlScheme> {

    @Test
    public void testHttpConstants() {
        assertSame(UrlScheme.HTTP, UrlScheme.with("http"));
    }

    @Test
    public void testHttpsConstants() {
        assertSame(UrlScheme.HTTPS, UrlScheme.with("https"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFirstCharFails() {
        this.createName("1http");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharFails() {
        this.createName("ab\u100cd");
    }

    @Test
    public void testIncludesPlusMinusDot() {
        this.createName("A123456789+-.abcABCxyzXYZ");
    }

    @Override
    protected UrlScheme createName(final String name) {
        return UrlScheme.with(name);
    }

    @Override
    protected Class<UrlScheme> type() {
        return UrlScheme.class;
    }
}
