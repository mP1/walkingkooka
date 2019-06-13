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
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.type.JavaVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UrlPathNameTest implements ClassTesting2<UrlPathName>,
        NameTesting<UrlPathName, UrlPathName>,
        SerializationTesting<UrlPathName> {

    @Override
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testEmpty() {
        assertSame(UrlPathName.ROOT, UrlPathName.with(""));
    }

    @Test
    public void testTooLongFails() {
        final char[] chars = new char[UrlPathName.MAXIMUM_LENGTH + 1];
        Arrays.fill(chars, 'x');

        assertThrows(IllegalArgumentException.class, () -> {
            UrlPathName.with(new String(chars));
        });
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc");
    }

    @Test
    public void testWithPercentage() {
        this.createNameAndCheck("abc%20def");
    }

    @Test
    public void testDifferentName() {
        this.checkNotEquals(UrlPathName.with("different"));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(UrlPathName.with("zzzzz"));
    }

    @Test
    public void testCaseSignificant() {
        this.checkNotEquals(UrlParameterName.with("ABC"));
    }

    @Test
    public void testToStringAddQuotesBecauseOfWhitespace() {
        final String string = "name with whitespace";
        final UrlPathName name = UrlPathName.with(string);
        assertEquals(CharSequences.quote(string).toString(), name.toString());
    }

    @Test
    public void testRootIsSingleton() throws Exception {
        this.serializeSingletonAndCheck(UrlPathName.ROOT);
    }

    @Override
    public UrlPathName createName(final String name) {
        return UrlPathName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "path";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "file";
    }

    @Override
    public Class<UrlPathName> type() {
        return UrlPathName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public UrlPathName serializableInstance() {
        return UrlPathName.with("name");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
