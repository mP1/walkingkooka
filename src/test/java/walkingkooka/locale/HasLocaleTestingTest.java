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

package walkingkooka.locale;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HasLocaleTestingTest implements HasLocaleTesting {

    @Test
    public void testLocaleAndCheck() {
        final Locale locale = Locale.ENGLISH;
        this.localeAndCheck(() -> locale, locale);
    }

    @Test
    public void testLocaleAndCheckFails() {
        boolean failed = false;
        try {
            this.localeAndCheck(() -> Locale.ENGLISH, Locale.FRANCE);
        } catch (final AssertionFailedError expected) {
            failed = true;
        }
        assertEquals(true, failed);
    }
}
