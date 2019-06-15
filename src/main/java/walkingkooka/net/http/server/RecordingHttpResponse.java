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

package walkingkooka.net.http.server;

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link HttpResponse} that records set status and added entities.
 */
public final class RecordingHttpResponse implements HttpResponse {

    /**
     * Creates an empty recording http response.
     */
    static RecordingHttpResponse with() {
        return new RecordingHttpResponse();
    }

    /**
     * Private ctor use factory.
     */
    private RecordingHttpResponse() {
        super();
    }

    @Override
    public void setStatus(final HttpStatus status) {
        Objects.requireNonNull(status, "status");
        this.status = status;
    }

    public Optional<HttpStatus> status() {
        return Optional.ofNullable(this.status);
    }

    private HttpStatus status;

    @Override
    public void addEntity(final HttpEntity entity) {
        Objects.requireNonNull(entity, "entity");
        this.entities.add(entity);
    }

    public List<HttpEntity> entities() {
        return Lists.readOnly(this.entities);
    }

    private final List<HttpEntity> entities = Lists.array();

    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.entities);
    }

    public boolean equals(final Object other) {
        return this == other ||
                other instanceof RecordingHttpResponse &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final RecordingHttpResponse other) {
        return Objects.equals(this.status, other.status) &&
                Objects.equals(this.entities, other.entities);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .valueSeparator("\n")
                .separator("\n")
                .value(this.status)
                .value(this.entities)
                .build();
    }
}
