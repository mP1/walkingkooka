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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptEncodingTest extends HeaderValue2TestCase<AcceptEncoding, List<Encoding>>
        implements ParseStringTesting<AcceptEncoding>,
        PredicateTesting<AcceptEncoding, ContentEncoding> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
           AcceptEncoding.with(null);
        });
    }

    // predicate.......................................................................................................

    @Test
    public void testTestWildcard() {
        this.testTrue(acceptEncoding(Encoding.WILDCARD_ENCODING), ContentEncoding.GZIP);
    }

    @Test
    public void testTestWildcard2() {
        this.testTrue(acceptEncoding(Encoding.WILDCARD_ENCODING), ContentEncoding.with("abc"));
    }

    @Test
    public void testTestDifferentWildcard() {
        this.testTrue(acceptEncoding(Encoding.with("different"), Encoding.WILDCARD_ENCODING), ContentEncoding.with("abc"));
    }

    @Test
    public void testTestDifferentNonWildcard() {
        this.testFalse(acceptEncoding(Encoding.with("different")), ContentEncoding.with("abc"));
    }

    @Test
    public void testTestNonWildcardDifferentCase() {
        this.testTrue(acceptEncoding(Encoding.with("ABC")), ContentEncoding.with("abc"));
    }

    @Test
    public void testTestNonWildcardSameCase() {
        this.testTrue(acceptEncoding(Encoding.with("xyz")), ContentEncoding.with("xyz"));
    }

    @Test
    public void testTestNonWildcardSameCase2() {
        this.testTrue(acceptEncoding(Encoding.with("different"), Encoding.with("ABC")), ContentEncoding.with("ABC"));
    }

    // parse...........................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("gzip, *;q=0.5",
                AcceptEncoding.with(Lists.of(Encoding.GZIP,
                Encoding.WILDCARD_ENCODING.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)))));
    }

    // helpers.......................................................................................................

    @Override
    AcceptEncoding createHeaderValue(final List<Encoding> value) {
        return AcceptEncoding.with(value);
    }

    private AcceptEncoding acceptEncoding(final Encoding... value) {
        return this.createHeaderValue(Lists.of(value));
    }

    @Override
    List<Encoding> value() {
        return Lists.of(Encoding.BR, Encoding.GZIP);
    }

    @Override
    List<Encoding> differentValue() {
        return Lists.of(Encoding.GZIP);
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return false;
    }

    @Override
    public Class<AcceptEncoding> type() {
        return AcceptEncoding.class;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public AcceptEncoding parse(final String text) {
        return AcceptEncoding.parse(text);
    }

    // Predicate.......................................................................................................

    @Override
    public AcceptEncoding createPredicate() {
        return this.createHeaderValue();
    }

    // ClassTestCase ............................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // TypeNaming ............................................................................................

    @Override
    public String typeNamePrefix() {
        return "AcceptEncoding";
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
