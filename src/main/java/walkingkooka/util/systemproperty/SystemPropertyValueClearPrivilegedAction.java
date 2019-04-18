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
 */

package walkingkooka.util.systemproperty;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Clear the current value of a system property when executed within a {@link
 * AccessController#doPrivileged(PrivilegedAction)}.
 */
final class SystemPropertyValueClearPrivilegedAction implements PrivilegedAction<Object> {

    /**
     * Reads a system property within a {@link AccessController#doPrivileged(PrivilegedAction)}
     * block.
     */
    static void clear(final SystemProperty property) {
        final ThreadLocal<SystemProperty> p = SystemPropertyValueClearPrivilegedAction.property;
        p.set(property);
        try {
            AccessController.doPrivileged(SystemPropertyValueClearPrivilegedAction.INSTANCE);
        } finally {
            property.set(null);
        }
    }

    /**
     * Singleton
     */
    private final static SystemPropertyValueClearPrivilegedAction INSTANCE
            = new SystemPropertyValueClearPrivilegedAction();

    /**
     * Thread local used to temporarily hold the system property being read.
     */
    final static ThreadLocal<SystemProperty> property = new ThreadLocal<SystemProperty>();

    /**
     * Private constructor
     */
    private SystemPropertyValueClearPrivilegedAction() {
        super();
    }

    @Override
    public Object run() {
        System.clearProperty(SystemPropertyValueClearPrivilegedAction.property.get().value());
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(SystemPropertyValueClearPrivilegedAction.property.get());
    }
}
