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

package walkingkooka.util.variable;

/**
 * Holds a changeable integer value.
 */
final public class IntegerVariable {

    /**
     * Creates a new {@link IntegerVariable} initialized to 0.
     */
    public static IntegerVariable create() {
        return new IntegerVariable();
    }

    /**
     * Use static factory
     */
    private IntegerVariable() {
        super();
    }

    private int value;

    /**
     * Gets the current value.
     */
    public int value() {
        return this.value;
    }

    /**
     * Adds 1 to this value.
     */
    public void increment() {
        this.value++;
    }

    /**
     * Adds the given value to the current. Overflow is ignored.
     */
    public void add(final int value) {
        this.value += value;
    }

    /**
     * Overwrites the current value with the new.
     */
    public void set(final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
