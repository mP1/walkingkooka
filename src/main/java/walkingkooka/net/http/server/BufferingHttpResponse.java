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

package walkingkooka.net.http.server;

import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An abstract {@link WrapperHttpResponse} that buffers any status until an entity is added.
 */
abstract class BufferingHttpResponse extends WrapperHttpResponse {

    /**
     * Package private to limit sub classing.
     */
    BufferingHttpResponse(final HttpResponse response) {
        super(response);
    }

    @Override
    public final void setStatus(final HttpStatus status) {
        Objects.requireNonNull(status, "status");

        if(this.committed) {
            this.response.setStatus(status);
        } else {
            this.status = status;
        }
    }

    @Override
    public final void addEntity(final HttpEntity entity) {
        Objects.requireNonNull(entity, "entity");

        if(this.committed) {
            this.response.addEntity(entity);
        } else {
            this.committed = true;
            this.addEntity(this.statusOrFail(), entity);
        }
    }

    private HttpStatus statusOrFail() {
        final HttpStatus status = this.status;
        if(null==status) {
            throw new IllegalStateException("Status missing");
        }
        return status;
    }

    private HttpStatus status;

    /**
     * When false the status is buffered and not set upon the wrapped {@link HttpResponse}.
     */
    boolean committed = false;

    /**
     * Called when a buffered entity and status is added.
     */
    abstract void addEntity(final HttpStatus status,
                            final HttpEntity entity);

    /**
     * Removes any content headers in the given entity.
     */
    final HttpEntity removeContentHeaders(final HttpEntity entity) {
        return entity.setHeaders(
                entity.headers()
                .entrySet()
                .stream()
                .filter(hav -> !hav.getKey().isContent())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}
