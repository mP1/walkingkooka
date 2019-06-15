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

package walkingkooka.util;

import walkingkooka.type.PublicStaticHelper;

final public class Waiter implements PublicStaticHelper {

    /**
     * Sleeps the current {@link Thread} for the duration specified.
     */
    public static void waitAtLeast(final long milliseconds) {
        final long until = System.currentTimeMillis() + Math.max(1, milliseconds);
        for (; ; ) {
            final long left = until - System.currentTimeMillis();
            if (left < 0) {
                break;
            }
            try {
                Thread.sleep(left);
            } catch (final InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stop creation.
     */
    private Waiter() {
        throw new UnsupportedOperationException();
    }
}
