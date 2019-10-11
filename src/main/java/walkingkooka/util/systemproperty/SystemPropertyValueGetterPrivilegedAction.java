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
 * Reads the current value of a system property when executed within a {@link
 * AccessController#doPrivileged(PrivilegedAction)}.
 */
final class SystemPropertyValueGetterPrivilegedAction implements PrivilegedAction<String> {

    /**
     * Reads a system property within a {@link AccessController#doPrivileged(PrivilegedAction)}
     * block.
     */
    static String get(final SystemProperty property) {
        final ThreadLocal<SystemProperty> variable
                = SystemPropertyValueGetterPrivilegedAction.property;
        variable.set(property);
        try {
            return AccessController.doPrivileged(SystemPropertyValueGetterPrivilegedAction.INSTANCE);
        } finally {
            variable.set(null);
        }
    }

    /**
     * Singleton
     */
    private final static SystemPropertyValueGetterPrivilegedAction INSTANCE
            = new SystemPropertyValueGetterPrivilegedAction();

    /**
     * Thread local used to temporarily hold the system property being read.
     */
    final static ThreadLocal<SystemProperty> property = new ThreadLocal<>();

    /**
     * Private constructor
     */
    private SystemPropertyValueGetterPrivilegedAction() {
        super();
    }

    @Override
    public String run() {
        return System.getProperty(SystemPropertyValueGetterPrivilegedAction.property.get().value());
    }

    @Override
    public String toString() {
        return SystemPropertyValueGetterPrivilegedAction.property.get().toString();
    }
}
