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
import walkingkooka.net.http.HttpHeaderScope;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class HeaderValueTestCase<V extends HeaderValue> extends PublicClassTestCase<V> {

    @Test
    public final void testHttpHeaderScopeValid() {
        final V value = this.createHeaderValue();
        assertNotEquals(value + " scope", HttpHeaderScope.UNKNOWN, value.httpHeaderScope());
    }

    @Test
    public final void testHttpHeaderScope() {
        final V value = this.createHeaderValue();
        assertEquals(value + " scope", this.httpHeaderScope(), value.httpHeaderScope());
    }

    abstract protected V createHeaderValue();

    abstract protected HttpHeaderScope httpHeaderScope();
}
