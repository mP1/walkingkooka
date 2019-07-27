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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HasMathContextTestingTest implements HasMathContextTesting {

    @Test
    public void testHasMathContextAndCheck() {
        final MathContext mathContext = MathContext.DECIMAL32;
        this.hasMathContextAndCheck(() -> mathContext, mathContext);
    }

    @Test
    public void testHasMathContextAndCheckFails() {
        boolean failed = false;
        try {
            this.hasMathContextAndCheck(() -> MathContext.DECIMAL64, MathContext.DECIMAL128);
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        assertEquals(true, failed);
    }
}
