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

import static org.junit.Assert.assertEquals;

public abstract class CharsetNameTestCase<N extends CharsetName> extends HeaderValueTestCase<N> {

    CharsetNameTestCase() {
        super();
    }

    @Test
    public final void testIsWildcard() {
        final N charset = this.createCharsetName();
        assertEquals(charset.toString(),
                CharsetNameWildcard.class.equals(this.type()),
                charset.isWildcard());
    }

    // matches.......................................................

    @Test(expected = HeaderValueException.class)
    public final void testMatchesWildcardFails() {
        this.createCharsetName().matches(CharsetName.WILDCARD_CHARSET);
    }

    final void matches(final CharsetName contentType,
                       final boolean matches) {
        this.matches(this.createCharsetName(),
                contentType,
                matches);
    }

    final void matches(final CharsetName charsetName,
                       final CharsetName contentType,
                       final boolean matches) {
        assertEquals(charsetName + " matches " + contentType,
                matches,
                charsetName.matches(contentType));
    }

    @Test
    public final void testHeaderText() {
        assertEquals(this.charsetNameToString(), this.createCharsetName().toHeaderText());
    }

    @Test
    public final void testToString() {
        assertEquals(this.charsetNameToString(), this.createCharsetName().toString());
    }

    abstract N createCharsetName();

    abstract String headerText();

    abstract String charsetNameToString();

    @Override
    protected final boolean typeMustBePublic() {
        return false;
    }
}
