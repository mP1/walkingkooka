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

package walkingkooka.reflect;

import javaemul.internal.annotations.GwtIncompatible;

/**
 * The static methods in this class shadow others in the super-class and will be removed by GWT/J2CL compiler.
 */
final class JavaNameIdentifier extends JavaNameIdentifierEmulated {

    @GwtIncompatible
    static boolean isStart(final char c) {
        return Character.isJavaIdentifierStart(c);
    }

    @GwtIncompatible
    static boolean isPart(final char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
