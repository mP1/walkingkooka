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

package walkingkooka.util.systemproperty;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Updates the current value of a system property when executed within a {@link
 * AccessController#doPrivileged(PrivilegedAction)}.
 */
final class SystemPropertyValueSetterPrivilegedAction implements PrivilegedAction<String> {

    /**
     * Reads a system property within a {@link AccessController#doPrivileged(PrivilegedAction)}
     * block.
     */
    static String set(final SystemProperty property, final String value) {
        return AccessController.doPrivileged(new SystemPropertyValueSetterPrivilegedAction(property, value));
    }

    /**
     * Private constructor
     */
    private SystemPropertyValueSetterPrivilegedAction(final SystemProperty property,
                                                      final String value) {
        super();
        this.property = property;
        this.value = value;
    }

    @Override
    public String run() {
        return System.setProperty(this.property.propertyValue(), this.value);
    }

    private final SystemProperty property;
    private final String value;

    @Override
    public String toString() {
        return this.property + "=" + this.value;
    }
}
