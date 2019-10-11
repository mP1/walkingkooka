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

package walkingkooka.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FieldNameTest extends JavaNameTestCase<FieldName> {

    @Test
    public void testFromFieldNullFails() {
        assertThrows(NullPointerException.class, () -> FieldName.from(null));
    }

    @Test
    public void testFromField() throws Exception {
        final String name = "field123";
        final FieldName field = FieldName.from(this.getClass().getDeclaredField(name));
        this.checkValue(field, name);
    }

    @SuppressWarnings("unused")
    private final String field123 = "value123";

    @Override
    public FieldName createName(final String name) {
        return FieldName.with(name);
    }

    @Override
    public String nameText() {
        return "field123";
    }

    @Override
    public String differentNameText() {
        return "differentField";
    }

    @Override
    public String nameTextLess() {
        return "before";
    }

    @Override
    public Class<FieldName> type() {
        return FieldName.class;
    }
}
