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

package walkingkooka.naming;

import walkingkooka.reflect.StaticHelper;

import java.util.List;

/**
 * Private Path helper extracted here because GWT does not support private methods on interfaces.
 */
final class PathHelper implements StaticHelper {

    static <N extends Name> void gatherNames(final Path<?, N> path,
                                             final List<N> names) {
        Path<?, N> parent = path.parent()
            .orElse(null);
        if (null != parent) {
            gatherNames(
                parent,
                names
            );
        }

        names.add(path.name());
    }

    /**
     * Private ctor
     */
    private PathHelper() {
        throw new UnsupportedOperationException();
    }
}
