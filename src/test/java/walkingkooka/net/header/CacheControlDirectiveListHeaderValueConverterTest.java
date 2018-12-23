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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;

import java.util.List;
import java.util.Optional;

public final class CacheControlDirectiveListHeaderValueConverterTest extends
        HeaderValueConverterTestCase<CacheControlDirectiveListHeaderValueConverter,
                        List<CacheControlDirective<?>>> {

    @Test
    public void testCacheControlRoundtrip() {
        this.parseAndToTextAndCheck("max-age=123, no-cache, no-store",
                Lists.of(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                        CacheControlDirective.NO_CACHE,
                        CacheControlDirective.NO_STORE));
    }

    @Override
    protected String requiredPrefix() {
        return CacheControlDirective.class.getSimpleName() + List.class.getSimpleName();
    }

    @Override
    protected String invalidHeaderValue() {
        return "extension=\"abc";
    }

    @Override
    protected String converterToString() {
        return "List<CacheControlDirective>";
    }

    @Override
    protected CacheControlDirectiveListHeaderValueConverter converter() {
        return CacheControlDirectiveListHeaderValueConverter.INSTANCE;
    }

    @Override
    protected Name name() {
        return HttpHeaderName.CACHE_CONTROL;
    }

    @Override
    protected List<CacheControlDirective<?>> value() {
        return Lists.of(CacheControlDirective.NO_CACHE, CacheControlDirective.NO_STORE);
    }

    @Override protected Class<CacheControlDirectiveListHeaderValueConverter> type() {
        return CacheControlDirectiveListHeaderValueConverter.class;
    }
}
