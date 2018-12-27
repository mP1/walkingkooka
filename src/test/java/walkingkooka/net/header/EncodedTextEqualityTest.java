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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Optional;

public final class EncodedTextEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<EncodedText> {

    @Test
    public void testDifferentCharset() {
        this.checkNotEquals(EncodedText.with(CharsetName.UTF_16,
                this.language(),
                this.value()));
    }

    @Test
    public void testDifferentLanguage() {
        this.checkNotEquals(EncodedText.with(this.charset(),
                Optional.of(LanguageTagName.with("fr")),
                this.value()));
    }

    @Test
    public void testDifferentLanguage2() {
        this.checkNotEquals(EncodedText.with(this.charset(),
                EncodedText.NO_LANGUAGE,
                this.value()));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(EncodedText.with(this.charset(),
                this.language(),
                "different"));
    }

    @Override
    protected EncodedText createObject() {
        return EncodedText.with(this.charset(),
                this.language(),
                this.value());
    }

    private CharsetName charset() {
        return CharsetName.UTF_8;
    }

    private Optional<LanguageTagName> language() {
        return Optional.of(LanguageTagName.with("en"));
    }

    private String value() {
        return "abc123";
    }
}
