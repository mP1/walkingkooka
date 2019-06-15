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

package walkingkooka.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Throws an exception with the provided message if an attempt is made to {@link #set} more than
 * once.
 */
final public class Latch {

    public static Latch create() {
        return new Latch();
    }

    private Latch() {
        this.flag = false;
    }

    // properties

    private boolean flag;

    /**
     * Setter that includes a guard that only allows the boolean state to be set once.
     */
    public void set(final String message) {
        assertFalse(this.value(), message);
        this.flag = true;
    }

    /**
     * Getter that returns the current state of this flag
     */
    public boolean value() {
        return this.flag;
    }

    /**
     * Resets the flag
     */
    public void reset() {
        this.flag = false;
    }

    /**
     * Returns the current boolean state.
     */
    @Override
    public String toString() {
        return String.valueOf(this.flag);
    }
}
