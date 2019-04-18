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

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.xml.XmlNode;

public final class TestHateosResource4 extends FakeHateosResource<String> {

    static TestHateosResource4 fromJsonNode(final JsonNode node) {
        return with(node.stringValueOrFail());
    }

    static TestHateosResource4 with(final String id) {
        return new TestHateosResource4(id);
    }

    private TestHateosResource4(final String id) {
        super();
        this.id = id;
    }

    @Override
    public String id() {
        return this.id;
    }

    private final String id;

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.id);
    }

    @Override
    public XmlNode toXmlNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TestHateosResource4 && equals0(Cast.to(other));
    }

    private boolean equals0(final TestHateosResource4 other) {
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
