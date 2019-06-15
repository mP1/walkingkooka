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

import walkingkooka.net.http.HttpEntity;

/**
 * An abstract {@link BufferingHttpResponse} that buffers but unconditionally adds non first parts.
 */
abstract class NonMultiPartAwareBufferingHttpResponse extends BufferingHttpResponse {

    /**
     * Package private to limit sub classing.
     */
    NonMultiPartAwareBufferingHttpResponse(final HttpResponse response) {
        super(response);
    }

    /**
     * Unconditionally adds parts that are not the first.
     */
    @Override
    final void addAdditionalEntity(final HttpEntity entity) {
        this.response.addEntity(entity);
    }
}
