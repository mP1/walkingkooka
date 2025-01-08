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

package walkingkooka.stream.push;

import walkingkooka.Cast;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class TestCloseableRunnable implements Runnable {

    static TestCloseableRunnable with(final String toString) {
        return new TestCloseableRunnable(toString);
    }

    private TestCloseableRunnable(final String toString) {
        super();
        this.toString = toString;
    }

    @Override
    public void run() {
        assertEquals(false, this.closed, "Already closed");
        this.closed = true;
    }

    void checkNotClosed() {
        assertEquals(false, this.closed, "The closeable should not be closed");
    }

    void checkClosed() {
        assertEquals(true, this.closed, "The closeable is not closed");
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString, this.closed);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TestCloseableRunnable &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TestCloseableRunnable other) {
        return this.toString.equals(other.toString) &&
            this.closed == other.closed;
    }

    @Override
    public String toString() {
        return toString;
    }

    boolean closed;

    private final String toString;
}
