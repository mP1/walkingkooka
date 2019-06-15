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

import walkingkooka.net.UrlPath;
import walkingkooka.net.header.MediaType;
import walkingkooka.type.PublicStaticHelper;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class HttpRequestHttpResponseBiConsumers implements PublicStaticHelper {

    /**
     * {@see DirectoryHttpRequestHttpResponseBiConsumer}
     */
    public static BiConsumer<HttpRequest, HttpResponse> directory(final UrlPath urlPathBase,
                                                                  final Path fileBase,
                                                                  final Function<FileResponse, MediaType> contentTypeIdentifier) {
        return DirectoryHttpRequestHttpResponseBiConsumer.with(urlPathBase, fileBase, contentTypeIdentifier);
    }

    /**
     * Stop creation
     */
    private HttpRequestHttpResponseBiConsumers() {
        throw new UnsupportedOperationException();
    }
}
