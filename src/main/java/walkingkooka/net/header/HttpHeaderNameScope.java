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

package walkingkooka.net.header;

enum HttpHeaderNameScope implements HasHeaderScope {

    REQUEST(false, true, false),
    RESPONSE(false, false, true),
    RESPONSE_MULTIPART(true, true, false),
    REQUEST_RESPONSE(false, true, true),
    REQUEST_RESPONSE_MULTIPART(true, true, true),
    UNKNOWN(true, true, true);

    HttpHeaderNameScope(final boolean multipart,
                        final boolean request,
                        final boolean response) {
        this.multipart = multipart;
        this.request = request;
        this.response = response;
    }

    @Override
    public final boolean isMultipart() {
        return this.multipart;
    }

    private final boolean multipart;

    @Override
    public final boolean isRequest() {
        return this.request;
    }

    private final boolean request;

    @Override
    public final boolean isResponse() {
        return this.response;
    }

    private final boolean response;
}
