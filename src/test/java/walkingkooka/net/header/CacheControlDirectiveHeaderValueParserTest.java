/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Optional;

public final class CacheControlDirectiveHeaderValueParserTest extends HeaderValueParserTestCase<CacheControlDirectiveHeaderValueParser,
        List<CacheControlDirective<?>>> {

    @Test
    public void testWildcardFails() {
        this.parseInvalidCharacterFails("*");
    }

    @Test
    public final void testParameterSeparatorFails() {
        this.parseInvalidCharacterFails(";");
    }

    @Test
    public final void testKeyValueSeparatorFails() {
        this.parseInvalidCharacterFails("=");
    }

    @Test
    public void testSlashFails() {
        this.parseInvalidCharacterFails("/");
    }

    @Test
    public void testValueSeparatorFails() {
        this.parseInvalidCharacterFails(",");
    }

    @Test
    public void testDirective() {
        this.parseAndCheck2("A", "A");
    }

    @Test
    public void testDirectiveParameterSeparatorFails() {
        this.parseMissingParameterValueFails("A=");
    }

    @Test
    public void testDirectiveParameterSeparatorWhitespaceFails() {
        this.parseInvalidCharacterFails("A= ");
    }

    @Test
    public void testDirectiveWhitespaceFails() {
        this.parseInvalidCharacterFails("A ");
    }

    @Test
    public void testDirectiveValueSeparatorFails() {
        this.parseInvalidCharacterFails("A,");
    }

    @Test
    public void testDirectiveValueSeparatorSpaceFails() {
        this.parseInvalidCharacterFails("A, ");
    }

    @Test
    public void testDirectiveKeyValueSeparatorUnclosedQuoteFails() {
        this.parseMissingClosingQuoteFails("A=\"");
    }

    @Test
    public void testDirectiveKeyValueSeparatorUnclosedQuoteFails2() {
        this.parseMissingClosingQuoteFails("A=\"BCD");
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterNonNumericValueFails() {
        this.parseInvalidCharacterFails("A=B");
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterNonNumericValueFails2() {
        this.parseInvalidCharacterFails("A=BCD", 'B');
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterNumericValue() {
        this.parseAndCheck2("A=1", "A", 1L);
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterNumericValue2() {
        this.parseAndCheck2("A=123", "A", 123L);
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterQuoteValueQuote() {
        this.parseAndCheck2("A=\"B\"", "A", "B");
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterQuoteValueQuote2() {
        this.parseAndCheck2("A=\"BCD\"", "A", "BCD");
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterQuoteValueQuote3() {
        this.parseAndCheck2("A=\"1\"", "A", "1");
    }

    @Test
    public void testDirectiveKeyValueSeparatorParameterQuoteValueQuoteValueFails() {
        final String text = "A=\"1\"\"2\"";
        this.parseInvalidCharacterFails(text, text.indexOf('2') - 1);
    }

    @Test
    public void testDirectiveCommentKeyValueSeparatorParameterNumericValue() {
        this.parseAndCheck2("(abc)A=1", "A", 1L);
    }

    // max-age.....................................................

    @Test
    public void testMaxAgeWithoutSecondsFails() {
        this.parseMissingParameterValueFails("max-age");
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
    public void testMaxStaleWithoutSecondsSeparatorFails() {
        this.parseInvalidCharacterFails("max-stale,");
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
        this.parseMissingParameterValueFails("must-revalidate=");
    }

    @Test
    public void testMustRevalidateSeparatorFails() {
        this.parseInvalidCharacterFails("must-revalidate,");
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
        this.parseMissingParameterValueFails("no-cache=");
    }

    @Test
    public void testNoCacheSeparatorFails() {
        this.parseInvalidCharacterFails("no-cache,");
    }

    @Test
    public void testNoCache() {
        this.parseAndCheck2("no-cache", "no-cache");
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
        this.parseMissingParameterValueFails("no-store=");
    }

    @Test
    public void testNoStoreSeparatorFails() {
        this.parseInvalidCharacterFails("no-store,");
    }

    @Test
    public void testNoStore() {
        this.parseAndCheck2("no-store", "no-store");
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
        this.parseMissingParameterValueFails("no-transform=");
    }

    @Test
    public void testNoTransformSeparatorFails() {
        this.parseInvalidCharacterFails("no-transform,");
    }

    @Test
    public void testNoTransform() {
        this.parseAndCheck2("no-transform", "no-transform");
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
        this.parseMissingParameterValueFails("public=");
    }

    @Test
    public void testPublicSeparatorFails() {
        this.parseInvalidCharacterFails("public,");
    }

    @Test
    public void testPublic() {
        this.parseAndCheck2("public", "public");
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
        this.parseMissingParameterValueFails("private=");
    }

    @Test
    public void testPrivateSeparatorFails() {
        this.parseInvalidCharacterFails("private,");
    }

    @Test
    public void testPrivate() {
        this.parseAndCheck2("private", "private");
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
        this.parseMissingParameterValueFails("proxy-revalidate=");
    }

    @Test
    public void testProxyRevalidateSeparatorFails() {
        this.parseInvalidCharacterFails("proxy-revalidate,");
    }

    @Test
    public void testProxyRevalidate() {
        this.parseAndCheck2("proxy-revalidate", "proxy-revalidate");
    }

    // s-maxage.....................................................

    @Test
    public void testSmaxAgeWithoutSecondsFails() {
        this.parseMissingParameterValueFails("s-maxage", 8);
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
    public void testMaxAgeSeparatorNoCache() {
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
    public void testNoCacheSeparatorSpaceCrNlNoStoreSeparatorNoTransform() {
        this.parseAndCheck3("no-cache,\r\n no-store,no-transform",
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
    public List<CacheControlDirective<?>> parse(final String text) {
        return CacheControlDirectiveHeaderValueParser.parseCacheControlDirectiveList(text);
    }

    @Override
    String valueLabel() {
        return CacheControlDirectiveHeaderValueParser.DIRECTIVE;
    }

    @Override
    public Class<CacheControlDirectiveHeaderValueParser> type() {
        return CacheControlDirectiveHeaderValueParser.class;
    }
}
