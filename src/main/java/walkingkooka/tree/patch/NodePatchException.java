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

package walkingkooka.tree.patch;

import walkingkooka.tree.TreeException;

/**
 * Base class for all exceptions thrown in this package.
 */
public class NodePatchException extends TreeException {

    private static final long serialVersionUID = 1L;

    protected NodePatchException() {
        super();
    }

    public NodePatchException(final String message) {
        super(message);
    }

    public NodePatchException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
