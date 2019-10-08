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

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Represents the key to a system property. A getter exists to retrieve the actual value.<br>
 * <a href="https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html"></a>
 */
final public class SystemProperty implements Value<String>, HashCodeEqualsDefined {

    /**
     * A cache of {@link SystemProperty}.
     */
    private final static Map<String, SystemProperty> PROPERTIES = new WeakHashMap<String, SystemProperty>();

    /**
     * A {@link String} containing an invalid value which if passed to the factory method will
     * fail.
     */
    public final static String INVALID = "";

    public final static SystemProperty FILE_ENCODING = createAndPut("file.encoding");

    public final static SystemProperty FILE_ENCODING_PKG = createAndPut("file.encoding.pkg");

    public final static SystemProperty FILE_SEPARATOR = createAndPut("file.separator");

    public final static SystemProperty JAVA_CLASS_PATH = createAndPut("java.class.path");

    public final static SystemProperty JAVA_CLASS_PATH_SEPARATOR = createAndPut("path.separator");

    public final static SystemProperty JAVA_CLASS_VERSION = createAndPut("java.class.version");

    public final static SystemProperty JAVA_COMPILER = createAndPut("java.compiler");

    public final static SystemProperty JAVA_HOME = createAndPut("java.home");

    public final static SystemProperty JAVA_IO_TMPDIR = createAndPut("java.io.tmpdir");

    public final static SystemProperty JAVA_VERSION = createAndPut("java.version");

    public final static SystemProperty JAVA_VENDOR = createAndPut("java.vendor");

    public final static SystemProperty JAVA_VENDOR_URL = createAndPut("java.vendor.url");

    public final static SystemProperty LINE_SEPARATOR = createAndPut("line.separator");

    public final static SystemProperty OS_NAME = createAndPut("os.name");

    public final static SystemProperty OS_ARCH = createAndPut("os.arch");

    public final static SystemProperty OS_VERSION = createAndPut("os.version");

    public final static SystemProperty USER_DIR = createAndPut("user.dir");

    public final static SystemProperty USER_HOME = createAndPut("user.home");

    public final static SystemProperty USER_LANGUAGE = createAndPut("user.language");

    public final static SystemProperty USER_NAME = createAndPut("user.name");

    public final static SystemProperty USER_REGION = createAndPut("user.region");

    public final static SystemProperty USER_TIMEZONE = createAndPut("user.timezone");

    // our properties

    /**
     * {@see walkingkooka.text.CaseSensitivity#FILE_SYSTEM_CASE_SENSITIVITY}
     */
    public final static SystemProperty FILE_SYSTEM_CASE_SENSITIVITY = createAndPut(CaseSensitivity.class.getName());

    /**
     * Retrieves a {@link SystemProperty} with the given name.
     */
    public static SystemProperty get(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        SystemProperty property = SystemProperty.PROPERTIES.get(name);
        if (null == property) {
            property = SystemProperty.createAndPut(name);
        }
        return property;
    }

    /**
     * Creates then updates the cache and returns the new {@link SystemProperty}.
     */
    synchronized private static SystemProperty createAndPut(final String name) {
        final SystemProperty property = new SystemProperty(name);
        SystemProperty.PROPERTIES.put(name, property);
        return property;
    }

    /**
     * Private constructor
     */
    private SystemProperty(final String name) {
        this.name = name;
    }

    /**
     * Gets the name of the system property of a {@link String}.
     */
    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Retrieves the current value of this system property. May return null if not set.
     */
    public String propertyValue() {
        String value;
        if (null == System.getSecurityManager()) {
            value = System.getProperty(this.name);
        } else {
            value = SystemPropertyValueGetterPrivilegedAction.get(this);
        }
        return value;
    }

    /**
     * Retrieves the current value of this property. If no value exists a {@link
     * MissingSystemPropertyException} rather than null will be thrown.
     */
    public String requiredPropertyValue() throws MissingSystemPropertyException {
        final String value = this.propertyValue();
        if (null == value) {
            throw new MissingSystemPropertyException("Unable to find value for=" + this.name);
        }
        return value;
    }

    /**
     * Sets or replaces the current value with the new value. A security manager may throw an
     * exception if the current user does not have permission.
     */
    public void set(final String value) {
        Objects.requireNonNull(value, "value");

        if (null == System.getSecurityManager()) {
            System.setProperty(this.name, value);
        } else {
            SystemPropertyValueSetterPrivilegedAction.set(this, value);
        }
    }

    /**
     * Clears the current system property value. A security manager may thrown an exception if the
     * current user does not have permission.
     */
    public String clear() {
        return System.clearProperty(this.name);
    }

    // Object

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof SystemProperty)
                && this.equals0((SystemProperty) other));
    }

    private boolean equals0(final SystemProperty other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return CharSequences.quoteIfNecessary(this.name).toString();
    }
}
