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
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Optional;

public final class CacheControlDirectiveHttpHeaderParserTest extends HeaderParserTestCase<CacheControlDirectiveHttpHeaderParser,
        List<CacheControlDirective<?>>> {

    @Test
    public void testSpaceFails() {
        this.parseInvalidCharacterFails(" ");
    }

    @Test
    public void testTabFails() {
        this.parseInvalidCharacterFails("\t");
    }

    @Test
    public void testInvalidCharacterFails() {
        this.parseInvalidCharacterFails(",");
    }

    @Test
    public void testDirective() {
        this.parseAndCheck2("A", "A");
    }

    @Test
    public void testDirectiveParameterSeparatorFails() {
        this.parseMissingParameterFails("A=");
    }

    @Test
    public void testDirectiveParameterSeparatorSpaceFails() {
        this.parseInvalidCharacterFails("A= ");
    }

    @Test
    public void testDirectiveParameterSeparatorTabFails() {
        this.parseInvalidCharacterFails("A=\t");
    }

    @Test
    public void testDirectiveSpaceFails() {
        this.parseInvalidCharacterFails("A ");
    }

    @Test
    public void testDirectiveTabFails() {
        this.parseInvalidCharacterFails("A\t");
    }

    @Test
    public void testDirectiveSeparator() {
        this.parseAndCheck2("A,", "A");
    }

    @Test
    public void testDirectiveSeparatorSpace() {
        this.parseAndCheck2("A, ", "A");
    }

    @Test
    public void testDirectiveSeparatorTab() {
        this.parseAndCheck2("A,\t", "A");
    }

    @Test
    public void testDirectiveParameterQuoteFails() {
        this.parseMissingClosingQuoteFails("A=\"");
    }

    @Test
    public void testDirectiveParameterQuoteInvalidCharFails() {
        this.parseInvalidCharacterFails("A=\";");
    }

    @Test
    public void testDirectiveParameterQuoteInvalidCharFails2() {
        this.parseInvalidCharacterFails("A=\"B;");
    }

    @Test
    public void testDirectiveParameterQuoteUnclosedFails() {
        this.parseMissingClosingQuoteFails("A=\"BCD");
    }

    @Test
    public void testDirectiveParameterNonNumericValueFails() {
        this.parseInvalidCharacterFails("A=B");
    }

    @Test
    public void testDirectiveParameterNonNumericValueFails2() {
        this.parseInvalidCharacterFails("A=BCD", 'B');
    }

    @Test
    public void testDirectiveParameterNumericValue() {
        this.parseAndCheck2("A=1", "A", 1L);
    }

    @Test
    public void testDirectiveParameterNumericValue2() {
        this.parseAndCheck2("A=123", "A", 123L);
    }

    @Test
    public void testDirectiveParameterQuoteValueQuote() {
        this.parseAndCheck2("A=\"B\"", "A", "B");
    }

    @Test
    public void testDirectiveParameterQuoteValueQuote2() {
        this.parseAndCheck2("A=\"BCD\"", "A", "BCD");
    }

    @Test
    public void testDirectiveParameterQuoteValueQuote3() {
        this.parseAndCheck2("A=\"1\"", "A", 1L); // contents of quotes gets detected as a Long
    }

    // max-age.....................................................

    @Test
    public void testMaxAgeWithoutSecondsFails() {
        this.parseMissingParameterFails("max-age");
    }

    @Test
    public void testMaxAgeWithoutSecondsSeparatorFails() {
        this.parseInvalidCharacterFails("max-age,");
    }

    @Test
    public void testMaxAgeWithoutSecondsSpaceSeparatorFails() {
        this.parseInvalidCharacterFails("max-age ");
    }

    @Test
    public void testMaxAgeWithoutSecondsTabSeparatorFails() {
        this.parseInvalidCharacterFails("max-age\t");
    }

    @Test
    public void testMaxAgeInvalidCharacterFails() {
        this.parseInvalidCharacterFails("max-age=!");
    }

    @Test
    public void testMaxAgeInvalidCharacterFails2() {
        this.parseInvalidCharacterFails("max-age=1!");
    }

    @Test
    public void testMaxAgeWithSeconds() {
        this.parseAndCheck2("max-age=1", "max-age", 1L);
    }

    @Test
    public void testMaxAgeWithSeconds2() {
        this.parseAndCheck2("max-age=23", "max-age", 23L);
    }

    // max-stale.....................................................

    @Test
    public void testMaxStaleWithoutSeconds() {
        this.parseAndCheck2("max-stale", "max-stale");
    }

    @Test
    public void testMaxStaleWithoutSecondsSeparator() {
        this.parseAndCheck2("max-stale,", "max-stale");
    }

    @Test
    public void testMaxStaleWithoutSecondsSpace() {
        this.parseInvalidCharacterFails("max-stale ");
    }

    @Test
    public void testMaxStaleWithoutSecondsTab() {
        this.parseInvalidCharacterFails("max-stale\t");
    }

    @Test
    public void testMaxStaleInvalidCharacterFails() {
        this.parseInvalidCharacterFails("max-stale=!");
    }

    @Test
    public void testMaxStaleInvalidCharacterFails2() {
        this.parseInvalidCharacterFails("max-stale=1!");
    }

    @Test
    public void testMaxStaleWithSeconds() {
        this.parseAndCheck2("max-stale=1", "max-stale", 1L);
    }

    @Test
    public void testMaxStaleWithSeconds2() {
        this.parseAndCheck2("max-stale=23", "max-stale", 23L);
    }

    // must-revalidate.....................................................

    @Test
    public void testMustRevalidateWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("must-revalidate ");
    }

    @Test
    public void testMustRevalidateWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("must-revalidate\t");
    }

    @Test
    public void testMustRevalidateParameterSeparatorFails() {
        this.parseInvalidCharacterFails("must-revalidate=");
    }

    @Test
    public void testMustRevalidateSeparator() {
        this.parseAndCheck2("must-revalidate,", "must-revalidate");
    }

    @Test
    public void testMustRevalidate() {
        this.parseAndCheck2("must-revalidate", "must-revalidate");
    }

    // no-cache.....................................................

    @Test
    public void testNoCacheWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("no-cache ");
    }

    @Test
    public void testNoCacheWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("no-cache\t");
    }

    @Test
    public void testNoCacheParameterSeparatorFails() {
        this.parseInvalidCharacterFails("no-cache=");
    }

    @Test
    public void testNoCache() {
        this.parseAndCheck2("no-cache", "no-cache");
    }

    @Test
    public void testNoCacheSeparator() {
        this.parseAndCheck2("no-cache,", "no-cache");
    }

    // no-store.....................................................

    @Test
    public void testNoStoreWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("no-store ");
    }

    @Test
    public void testNoStoreWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("no-store\t");
    }

    @Test
    public void testNoStoreParameterSeparatorFails() {
        this.parseInvalidCharacterFails("no-store=");
    }

    @Test
    public void testNoStore() {
        this.parseAndCheck2("no-store", "no-store");
    }

    @Test
    public void testNoStoreSeparator() {
        this.parseAndCheck2("no-store,", "no-store");
    }

    // no-transform.....................................................

    @Test
    public void testNoTransformWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("no-transform ");
    }

    @Test
    public void testNoTransformWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("no-transform\t");
    }

    @Test
    public void testNoTransformParameterSeparatorFails() {
        this.parseInvalidCharacterFails("no-transform=");
    }

    @Test
    public void testNoTransform() {
        this.parseAndCheck2("no-transform", "no-transform");
    }

    @Test
    public void testNoTransformSeparator() {
        this.parseAndCheck2("no-transform,", "no-transform");
    }

    // public.....................................................

    @Test
    public void testPublicWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("public ");
    }

    @Test
    public void testPublicWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("public\t");
    }

    @Test
    public void testPublicParameterSeparatorFails() {
        this.parseInvalidCharacterFails("public=");
    }


    @Test
    public void testPublic() {
        this.parseAndCheck2("public", "public");
    }

    @Test
    public void testPublicSeparator() {
        this.parseAndCheck2("public,", "public");
    }

    // private.....................................................

    @Test
    public void testPrivateWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("private ");
    }

    @Test
    public void testPrivateWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("private\t");
    }

    @Test
    public void testPrivateParameterSeparatorFails() {
        this.parseInvalidCharacterFails("private=");
    }

    @Test
    public void testPrivate() {
        this.parseAndCheck2("private", "private");
    }

    @Test
    public void testPrivateSeparator() {
        this.parseAndCheck2("private,", "private");
    }

    // proxy-revalidate.....................................................

    @Test
    public void testProxyRevalidateWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("proxy-revalidate ");
    }

    @Test
    public void testProxyRevalidateWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("proxy-revalidate\t");
    }

    @Test
    public void testProxyRevalidateParameterSeparatorFails() {
        this.parseInvalidCharacterFails("proxy-revalidate=");
    }

    @Test
    public void testProxyRevalidate() {
        this.parseAndCheck2("proxy-revalidate", "proxy-revalidate");
    }

    @Test
    public void testProxyRevalidateSeparator() {
        this.parseAndCheck2("proxy-revalidate,", "proxy-revalidate");
    }

    // s-maxage.....................................................

    @Test
    public void testSmaxAgeWithoutSecondsFails() {
        this.parseMissingParameterFails("s-maxage", 8);
    }

    @Test
    public void testSmaxAgeWithoutSecondsSeparatorFails() {
        this.parseInvalidCharacterFails("s-maxage,");
    }

    @Test
    public void testSmaxAgeWithoutSecondsSpaceFails() {
        this.parseInvalidCharacterFails("s-maxage ");
    }

    @Test
    public void testSmaxAgeWithoutSecondsTabFails() {
        this.parseInvalidCharacterFails("s-maxage\t");
    }

    @Test
    public void testSmaxAgeInvalidCharacterFails() {
        this.parseInvalidCharacterFails("s-maxage=!");
    }

    @Test
    public void testSmaxAgeInvalidCharacterFails2() {
        this.parseInvalidCharacterFails("s-maxage=1!");
    }

    @Test
    public void testSmaxAgeWithSeconds() {
        this.parseAndCheck2("s-maxage=1", "s-maxage", 1L);
    }

    @Test
    public void testSmaxAgeWithSeconds2() {
        this.parseAndCheck2("s-maxage=23", "s-maxage", 23L);
    }

    // custom .....................................................................

    @Test
    public void testCustomQuotedValues() {
        this.parseAndCheck2("custom=\"abc\"",
                "custom",
                "abc");
    }

    @Test
    public void testCustomUnquotedValuesFails() {
        this.parseInvalidCharacterFails("custom=abc", 'a');
    }

    @Test
    public void testImmutable() {
        this.parseAndCheck2("immutable", "immutable");
    }

    @Test
    public void testStaleWhileRevalidateNumber() {
        this.parseAndCheck2("stale-while-revalidate=123", "stale-while-revalidate", 123L);
    }

    @Test
    public void testStaleIfError() {
        this.parseAndCheck2("stale-if-error=456", "stale-if-error", 456L);
    }

    // several ....................................................................

    @Test
    public void testMaxAgeSeparatorrNoCache() {
        this.parseAndCheck3("max-age=123,no-cache",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                CacheControlDirective.NO_CACHE);
    }

    @Test
    public void testMaxAgeSpaceNoCache() {
        this.parseAndCheck3("max-age=123, no-cache",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                CacheControlDirective.NO_CACHE);
    }

    @Test
    public void testMaxAgeTabNoCache() {
        this.parseAndCheck3("max-age=123,\tno-cache",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                CacheControlDirective.NO_CACHE);
    }

    @Test
    public void testMaxAgeSeparatorMaxStale() {
        this.parseAndCheck3("max-age=123,max-stale=456",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                CacheControlDirectiveName.MAX_STALE.setParameter(Optional.of(456L)));
    }

    @Test
    public void testNoCacheSeparatorNoStoreSeparatorNoTransform() {
        this.parseAndCheck3("no-cache,no-store,no-transform",
                CacheControlDirective.NO_CACHE,
                CacheControlDirective.NO_STORE,
                CacheControlDirective.NO_TRANSFORM);
    }

    @Test
    public void testNoCacheSeparatorSpaceNoStoreSeparatorSpaceNoTransform() {
        this.parseAndCheck3("no-cache, no-store, no-transform",
                CacheControlDirective.NO_CACHE,
                CacheControlDirective.NO_STORE,
                CacheControlDirective.NO_TRANSFORM);
    }

    @Test
    public void testNoCacheSeparatorTabNoStoreSeparatorTabNoTransform() {
        this.parseAndCheck3("no-cache, no-store, no-transform",
                CacheControlDirective.NO_CACHE,
                CacheControlDirective.NO_STORE,
                CacheControlDirective.NO_TRANSFORM);
    }

    @Test
    public void testNoCacheSeparatorSpaceTabSpaceTabNoStoreSeparatorNoTransform() {
        this.parseAndCheck3("no-cache, \t \tno-store,no-transform",
                CacheControlDirective.NO_CACHE,
                CacheControlDirective.NO_STORE,
                CacheControlDirective.NO_TRANSFORM);
    }

    @Test
    public void testExtensionQuotedParameterSeparatorNoTransform() {
        this.parseAndCheck3("extension=\"abc\",no-transform",
                CacheControlDirectiveName.with("extension").setParameter(Cast.to(Optional.of("abc"))),
                CacheControlDirective.NO_TRANSFORM);
    }

    // helpers ...........................................................................

    private void parseMissingParameterFails(final String text) {
        this.parseMissingParameterFails(text, text.length());
    }

    private void parseMissingParameterFails(final String text, final int at) {
        this.parseFails(text, CacheControlDirectiveHttpHeaderParser.missingParameter(at, text));
    }

    private void parseMissingClosingQuoteFails(final String text) {
        this.parseFails(text, CacheControlDirectiveHttpHeaderParser.missingClosingQuote(text));
    }

    private void parseAndCheck2(final String text, final String directive) {
        this.parseAndCheck3(text,
                CacheControlDirective.with(CacheControlDirectiveName.with(directive), Optional.empty()));
    }

    private void parseAndCheck2(final String text, final String directive, final Object value) {
        this.parseAndCheck3(text,
                CacheControlDirective.with(Cast.to(CacheControlDirectiveName.with(directive)), Optional.of(value)));
    }

    private void parseAndCheck3(final String text, final CacheControlDirective... directives) {
        this.parseAndCheck(text, Lists.of(directives));
    }

    @Override
    List<CacheControlDirective<?>> parse(final String text) {
        return CacheControlDirectiveHttpHeaderParser.parseCacheControlDirectiveList(text);
    }

    @Override
    protected Class<CacheControlDirectiveHttpHeaderParser> type() {
        return CacheControlDirectiveHttpHeaderParser.class;
    }
}
