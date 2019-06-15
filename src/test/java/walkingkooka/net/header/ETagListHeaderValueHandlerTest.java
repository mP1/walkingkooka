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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ETagListHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<ETagListHeaderValueHandler, List<ETag>> {

    @Override
    public String typeNamePrefix() {
        return ETag.class.getSimpleName();
    }

    @Test
    public void testParseETagOne() {
        this.parseAndToTextAndCheck("W/\"123\"",
                Lists.of(ETag.with("123", ETagValidator.WEAK)));
    }

    @Test
    public void testParseETagSeveral() {
        this.toTextAndCheck(Lists.of(ETag.with("123", ETagValidator.WEAK),
                ETag.with("456", ETagValidator.WEAK)), "W/\"123\", W/\"456\"");
    }

    @Test
    public void testCheckIncludesNullFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.etag(), null));
        });
    }

    @Test
    public void testCheckIncludesWrongTypeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.etag(), "WRONG!"));
        });
    }

    private ETag etag() {
        return ETag.with("value", ETagValidator.WEAK);
    }

    @Override
    ETagListHeaderValueHandler handler() {
        return ETagListHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<List<ETag>> name() {
        return HttpHeaderName.IF_MATCH;
    }

    @Override
    String invalidHeaderValue() {
        return "I/";
    }

    @Override
    List<ETag> value() {
        return ETag.parseList("\"1\",\"2\"");
    }

    @Override
    String valueType() {
        return this.listValueType(ETag.class);
    }

    @Override
    String handlerToString() {
        return "List<ETag>";
    }

    @Override
    public Class<ETagListHeaderValueHandler> type() {
        return ETagListHeaderValueHandler.class;
    }
}
