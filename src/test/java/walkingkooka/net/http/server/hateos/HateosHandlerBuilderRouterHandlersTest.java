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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HateosHandlerBuilderRouterHandlersTest extends ClassTestCase<HateosHandlerBuilderRouterHandlers<JsonNode>>
        implements ToStringTesting<HateosHandlerBuilderRouterHandlers<JsonNode>> {

    @Test
    public void testCopy() {
        final HateosHandlerBuilderRouterHandlers<JsonNode> handlers = HateosHandlerBuilderRouterHandlers.with();

        final FakeHateosGetHandler<JsonNode> get = new FakeHateosGetHandler<>();
        final FakeHateosPostHandler<JsonNode> post = new FakeHateosPostHandler<>();
        final FakeHateosPutHandler<JsonNode> put = new FakeHateosPutHandler<>();
        final FakeHateosDeleteHandler<JsonNode> delete = new FakeHateosDeleteHandler<>();

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

        final FakeHateosGetHandler<JsonNode> get = new FakeHateosGetHandler<JsonNode>() {
            public String toString() {
                return "G1";
            }

            ;
        };
        final FakeHateosPostHandler<JsonNode> post = new FakeHateosPostHandler<JsonNode>() {
            public String toString() {
                return "P2";
            }

            ;
        };
        final FakeHateosPutHandler<JsonNode> put = new FakeHateosPutHandler<JsonNode>() {
            public String toString() {
                return "P3";
            }

            ;
        };
        final FakeHateosDeleteHandler<JsonNode> delete = new FakeHateosDeleteHandler<JsonNode>() {
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

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
