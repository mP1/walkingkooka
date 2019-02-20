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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HateosHandlerBuilderRouterHandlersTest implements ClassTesting2<HateosHandlerBuilderRouterHandlers<JsonNode>>,
        ToStringTesting<HateosHandlerBuilderRouterHandlers<JsonNode>> {

    @Test
    public void testCopy() {
        final HateosHandlerBuilderRouterHandlers<JsonNode> handlers = HateosHandlerBuilderRouterHandlers.with();

        final FakeHateosGetHandler<BigInteger, JsonNode> get = new FakeHateosGetHandler<>();
        final FakeHateosPostHandler<BigInteger, JsonNode> post = new FakeHateosPostHandler<>();
        final FakeHateosPutHandler<BigInteger, JsonNode> put = new FakeHateosPutHandler<>();
        final FakeHateosDeleteHandler<BigInteger, JsonNode> delete = new FakeHateosDeleteHandler<>();

        handlers.get = get;
        handlers.post = post;
        handlers.put = put;
        handlers.delete = delete;

        final HateosHandlerBuilderRouterHandlers<JsonNode> copy = handlers.copy();

        assertEquals(get, copy.get, "GET");
        assertEquals(post, copy.post, "POST");
        assertEquals(put, copy.put, "PUT");
        assertEquals(delete, copy.delete, "DELETE");
    }

    @Test
    public void testToString() {
        final HateosHandlerBuilderRouterHandlers<JsonNode> handlers = HateosHandlerBuilderRouterHandlers.with();

        final FakeHateosGetHandler<BigInteger, JsonNode> get = new FakeHateosGetHandler<BigInteger, JsonNode>() {
            public String toString() {
                return "G1";
            }

            ;
        };
        final FakeHateosPostHandler<BigInteger, JsonNode> post = new FakeHateosPostHandler<BigInteger, JsonNode>() {
            public String toString() {
                return "P2";
            }

            ;
        };
        final FakeHateosPutHandler<BigInteger, JsonNode> put = new FakeHateosPutHandler<BigInteger, JsonNode>() {
            public String toString() {
                return "P3";
            }

            ;
        };
        final FakeHateosDeleteHandler<BigInteger, JsonNode> delete = new FakeHateosDeleteHandler<BigInteger, JsonNode>() {
            public String toString() {
                return "D4";
            }

            ;
        };

        handlers.get = get;
        handlers.post = post;
        handlers.put = put;
        handlers.delete = delete;

        this.toStringAndCheck(handlers, "GET=G1 POST=P2 PUT=P3 DELETE=D4");
    }

    @Override
    public Class<HateosHandlerBuilderRouterHandlers<JsonNode>> type() {
        return Cast.to(HateosHandlerBuilderRouterHandlers.class);
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
