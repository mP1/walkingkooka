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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;

import java.util.Optional;

public final class CacheControlHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<CacheControlHeaderValueHandler, CacheControl> {

    @Test
    public void testCacheControlRoundtrip() {
        this.parseAndToTextAndCheck("max-age=123, no-cache, no-store",
                CacheControl.with(Lists.of(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                        CacheControlDirective.NO_CACHE,
                        CacheControlDirective.NO_STORE)));
    }

    @Override
    public String typeNamePrefix() {
        return CacheControl.class.getSimpleName();
    }

    @Override
    String invalidHeaderValue() {
        return "extension=\"abc";
    }

    @Override
    String handlerToString() {
        return CacheControl.class.getSimpleName();
    }

    @Override
    CacheControlHeaderValueHandler handler() {
        return CacheControlHeaderValueHandler.INSTANCE;
    }

    @Override
    Name name() {
        return HttpHeaderName.CACHE_CONTROL;
    }

    @Override
    CacheControl value() {
        return CacheControl.with(Lists.of(CacheControlDirective.NO_CACHE, CacheControlDirective.NO_STORE));
    }

    @Override
    String valueType() {
        return this.valueType(CacheControl.class);
    }

    @Override
    public Class<CacheControlHeaderValueHandler> type() {
        return CacheControlHeaderValueHandler.class;
    }
}
