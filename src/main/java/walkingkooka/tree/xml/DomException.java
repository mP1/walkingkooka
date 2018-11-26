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

package walkingkooka.tree.xml;

import walkingkooka.SystemException;

/**
 * Base class for any {@link DomNode} related operation
 */
public class DomException extends SystemException {

    protected DomException() {
        super();
    }

    public DomException(final String message) {
        super(message);
    }

    public DomException(final String message, final Throwable cause) {
        super(message, cause);
    }

    // Serializable

    private static final long serialVersionUID = 327169759890631100L;
}
