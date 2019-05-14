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
 *
 */

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.util.Comparator;

public final class MaxPushableStreamStreamTerminalPushableStreamConsumerTest extends MaxOrMinPushableStreamStreamTerminalPushableStreamConsumerTestCase<MaxPushableStreamStreamTerminalPushableStreamConsumer<String>> {

    @Test
    public void testAcceptTwoValues() {
        this.acceptAndCheck("ab,cd", "cd");
    }

    @Test
    public void testAcceptTwoValues2() {
        this.acceptAndCheck("AB,cd", "cd");
    }

    @Test
    public void testAcceptSeveralValues() {
        this.acceptAndCheck("AB,cd,EF", "EF");
    }

    @Test
    public void testAcceptSeveralValues2() {
        this.acceptAndCheck("cd,EF,AB", "EF");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamStreamPushableStreamConsumer(), "max " + this.comparator() + " closeables: " + this.closeables);
    }

    @Override
    MaxPushableStreamStreamTerminalPushableStreamConsumer<String> createPushableStreamStreamPushableStreamConsumer(final Comparator<String> comparator,
                                                                                                                   final CloseableCollection closeables) {
        return MaxPushableStreamStreamTerminalPushableStreamConsumer.with(comparator, closeables);
    }

    @Override
    public Class<MaxPushableStreamStreamTerminalPushableStreamConsumer<String>> type() {
        return Cast.to(MaxPushableStreamStreamTerminalPushableStreamConsumer.class);
    }
}
