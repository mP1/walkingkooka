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

package walkingkooka.net.http;

import walkingkooka.NeverError;

/**
 * A grouping or categorization of status code.
 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes"></a>
 */
public enum HttpStatusCodeCategory {
    /**
     * 1xx (Informational): The request was received, continuing process
     */
    INFORMATION,
    /**
     * 2xx (Successful): The request was successfully received, understood, and accepted
     */
    SUCCESSFUL,
    /**
     * 3xx (Redirection): Further action needs to be taken in order to complete the request
     */
    REDIRECTION,
    /**
     * 4xx (Client Error): The request contains bad syntax or cannot be fulfilled
     */
    CLIENT_ERROR,
    /**
     * 5xx (Server Error): The server failed to fulfill an apparently valid request
     */
    SERVER_ERROR;

    /**
     * Used to select the category for {@link HttpStatusCode#category()}
     */
    static HttpStatusCodeCategory category(final int code) {
        HttpStatusCodeCategory category = null;

        switch (code / 100) {
            case 1:
                category = INFORMATION;
                break;
            case 2:
                category = SUCCESSFUL;
                break;
            case 3:
                category = REDIRECTION;
                break;
            case 4:
                category = CLIENT_ERROR;
                break;
            case 5:
                category = SERVER_ERROR;
                break;
            default:
                NeverError.unhandledCase(code, HttpStatusCodeCategory.values());
        }
        return category;
    }
}
