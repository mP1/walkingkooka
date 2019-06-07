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

import walkingkooka.Cast;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;

/**
 * A {@link HttpResponse} wrapper only allows multiple entities to be added if the first was a multipart.
 */
final class MultiPartAwareHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link MultiPartAwareHttpResponse} if the request has a range header.
     */
    static MultiPartAwareHttpResponse with(final HttpResponse response) {
        check(response);

        return response instanceof MultiPartAwareHttpResponse ?
                Cast.to(response) :
                new MultiPartAwareHttpResponse(response);
    }

    /**
     * Private ctor use factory
     */
    private MultiPartAwareHttpResponse(final HttpResponse response) {
        super(response);
    }

    /**
     * Assumes this is called for the first {@link HttpEntity} only and sets a flag if the response is a multipart.
     */
    @Override
    void addFirstEntity(final HttpStatus status,
                        final HttpEntity entity) {
        this.multipart = HttpHeaderName.CONTENT_TYPE.headerValue(entity.headers())
                .map(this::isMultipart)
                .orElse(Boolean.FALSE);
        this.response.setStatus(status);
        this.response.addEntity(entity);
     }

    private boolean isMultipart(final MediaType mediaType) {
        return mediaType.type().equals(MediaType.MULTIPART_BYTE_RANGES.type());
    }

    /**
     * If the first part is not a multipart ignores additional parts, without signally an errors.
     */
    @Override
    void addAdditionalEntity(final HttpEntity entity) {
        if(this.multipart) {
            this.response.addEntity(entity);
        }
    }

    private boolean multipart;
}
