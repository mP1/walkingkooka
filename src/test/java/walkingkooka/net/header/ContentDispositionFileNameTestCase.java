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

import org.junit.jupiter.api.Test;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ContentDispositionFileNameTestCase<F extends ContentDispositionFileName> extends
        HeaderValueTestCase<F> {

    ContentDispositionFileNameTestCase() {
        super();
    }

    final void check(final ContentDispositionFileName filename,
                     final String value,
                     final Optional<CharsetName> charsetName,
                     final Optional<LanguageTagName> language) {
        assertEquals(value, filename.value(), "value");
        assertEquals(charsetName, filename.charsetName(), "charsetName");
        assertEquals(language, filename.language(), "language");
    }

    @Test
    public final void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    @Override
    public final boolean isMultipart() {
        return true;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
