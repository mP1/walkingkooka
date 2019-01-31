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

package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OptionalsTest extends PublicStaticHelperTestCase<Optionals> {

    @Test
    public void testStreamNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Optionals.stream(null);
        });
    }

    @Test
    public void testStreamOptionalEmpty() {
        streamAndCheck(Optional.empty());
    }

    @Test
    public void testStreamOptionalNotEmpty() {
        streamAndCheck(Optional.of("abc123"));
    }

    private static void streamAndCheck(final Optional<?> optional) {
        assertEquals(optional.isPresent() ? Lists.of(optional.get()) : Lists.empty(),
                Optionals.stream(optional).collect(Collectors.toList()));
    }

    @Override
    protected Class<Optionals> type() {
        return Optionals.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
