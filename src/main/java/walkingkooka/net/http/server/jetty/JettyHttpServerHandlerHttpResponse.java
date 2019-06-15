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

package walkingkooka.net.http.server.jetty;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.server.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

final class JettyHttpServerHandlerHttpResponse implements HttpResponse {

    static JettyHttpServerHandlerHttpResponse create() {
        return new JettyHttpServerHandlerHttpResponse();
    }

    private JettyHttpServerHandlerHttpResponse() {
        super();
    }

    @Override
    public void setStatus(final HttpStatus status) {
        Objects.requireNonNull(status, "status");
        this.status = status;
    }

    private HttpStatus status;

    @Override
    public void addEntity(final HttpEntity entity) {
        Objects.requireNonNull(entity, "entity");
        this.entities.add(entity);
    }

    private final List<HttpEntity> entities = Lists.array();

    /**
     * Copies the status and entities to the given {@link HttpServletResponse}
     */
    void commit(final HttpServletResponse response) throws IOException, ServletException {
        final HttpStatus status = this.status;
        if(null==status) {
            throw new ServletException("Request not handled");
        }
        response.setStatus(status.value().code(), status.message());

        boolean first = true;
        MediaType contentType = null;

        try(final ServletOutputStream output = response.getOutputStream()) {
            for(HttpEntity entity : this.entities) {
                if(first) {
                    contentType = copyHeaders(entity, response);
                    copyBody(entity, output);
                    first = true;
                } else {

                }
            }
            output.flush();
        }
    }

    private static MediaType copyHeaders(final HttpEntity entity, final HttpServletResponse response) throws IOException {
        MediaType contentType = null;

        for(Entry<HttpHeaderName<?>, ?> headerAndValues : entity.headers().entrySet()) {
            final HttpHeaderName<?> headerName = headerAndValues.getKey();
            final Object headerValue = headerAndValues.getValue();

            response.addHeader(headerName.value(),
                    headerName.headerText(Cast.to(headerValue)));

            if(HttpHeaderName.CONTENT_TYPE.equals(headerName)) {
                contentType = MediaType.class.cast(headerName.checkValue(headerValue));
            }
        }

        return contentType;
    }

    private static void copyBody(final HttpEntity entity, final ServletOutputStream output)throws IOException {
        output.write(entity.body().value());
    }
}
