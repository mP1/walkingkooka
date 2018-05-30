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

import walkingkooka.util.Pair;

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
        final ThreadLocal<Pair<SystemProperty, String>> nameAndValue
                = SystemPropertyValueSetterPrivilegedAction.nameAndValue;
        nameAndValue.set(Pair.with(property, value));
        try {
            return AccessController.doPrivileged(SystemPropertyValueSetterPrivilegedAction.INSTANCE);
        } finally {
            nameAndValue.set(null);
        }
    }

    /**
     * Singleton
     */
    private final static SystemPropertyValueSetterPrivilegedAction INSTANCE
            = new SystemPropertyValueSetterPrivilegedAction();

    /**
     * Thread local used to temporarily hold the system property being set.
     */
    final static ThreadLocal<Pair<SystemProperty, String>> nameAndValue
            = new ThreadLocal<Pair<SystemProperty, String>>();

    /**
     * Private constructor
     */
    private SystemPropertyValueSetterPrivilegedAction() {
        super();
    }

    @Override
    public String run() {
        final Pair<SystemProperty, String> setting
                = SystemPropertyValueSetterPrivilegedAction.nameAndValue.get();
        return System.setProperty(setting.first().value(), setting.second());
    }

    @Override
    public String toString() {
        return SystemPropertyValueSetterPrivilegedAction.nameAndValue.get().toString();
    }
}
