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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PropertiesNameTest implements ClassTesting2<PropertiesName>,
        NameTesting<PropertiesName, PropertiesName>,
        SerializationTesting<PropertiesName> {

    @Test
    public void testCreateContainsSeparatorFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PropertiesName.with("xyz" + PropertiesPath.SEPARATOR.string());
        });
    }

    @Override
    public PropertiesName createName(final String name) {
        return PropertiesName.with(name);
    }

    @Override
    public Class<PropertiesName> type() {
        return PropertiesName.class;
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "b";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "a";
    }

    @Override
    public PropertiesName serializableInstance() {
        return PropertiesName.with("abc");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
