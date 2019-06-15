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

package walkingkooka.net;

import walkingkooka.Binary;
import walkingkooka.io.serialize.SerializationProxy;
import walkingkooka.net.header.MediaType;

import java.util.Optional;

final class DataUrlSerializationProxy implements SerializationProxy {

    static DataUrlSerializationProxy with(final DataUrl url) {
        return new DataUrlSerializationProxy(url);
    }

    private DataUrlSerializationProxy(final DataUrl url) {
        super();
        this.mediaType = url.mediaType().orElse(null);
        this.binary = url.binary();
    }

    private final MediaType mediaType;
    private final Binary binary;

    @Override
    public String toString() {
        return this.readResolve().toString();
    }

    // Serializable.....................................................................................................

    private Object readResolve() {
        return DataUrl.with(Optional.ofNullable(this.mediaType), this.binary);
    }

    private static final long serialVersionUID = 1L;
}
