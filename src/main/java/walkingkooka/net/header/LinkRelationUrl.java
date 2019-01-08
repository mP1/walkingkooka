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

import walkingkooka.net.AbsoluteUrl;

/**
 * Holds a link relation that holds a URN (AbsoluteUrl) value.
 */
final class LinkRelationUrl extends LinkRelation<AbsoluteUrl> {

    /**
     * Returns a {@link LinkRelation}
     */
    static LinkRelationUrl url(final String value) {
        return new LinkRelationUrl(AbsoluteUrl.parse(value));
    }

    /**
     * Package private to limit sub classing, use a constant or factory.
     */
    private LinkRelationUrl(final AbsoluteUrl value) {
        super(value);
    }

    @Override
    public boolean isUrl() {
        return true;
    }

    // HeaderValue ....................................................................................................

    @Override
    public final String toHeaderText() {
        return this.value().toString();
    }

    // Object ............................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LinkRelationUrl;
    }
}
