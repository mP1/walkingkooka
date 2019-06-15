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

import java.util.Objects;

/**
 * Exception thrown when a patch apply operation fails. A message and optional cause and the actual patch component that failed will be included.
 */
public class ApplyNodePatchException extends NodePatchException {

    private static final long serialVersionUID = 1L;

    public ApplyNodePatchException(final String message,
                                   final NodePatch<?, ?> patch) {
        super(message);
        this.patch = checkPatch(patch);
    }

    public ApplyNodePatchException(final String message,
                                   final NodePatch<?, ?> patch,
                                   final Throwable cause) {
        super(message, cause);
        this.patch = checkPatch(patch);
    }

    private static NodePatch<?, ?> checkPatch(final NodePatch<?, ?> patch) {
        return Objects.requireNonNull(patch, "patch");
    }

    public NodePatch<?, ?> patch() {
        return this.patch;
    }

    private final NodePatch<?, ?> patch;
}
