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

import org.junit.Test;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;

final public class PropertiesNameTest extends NameTestCase<PropertiesName, PropertiesName>
        implements SerializationTesting<PropertiesName> {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContainsSeparatorFails() {
        PropertiesName.with("xyz" + PropertiesPath.SEPARATOR.string());
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc");
    }

    @Override
    protected PropertiesName createName(final String name) {
        return PropertiesName.with(name);
    }

    @Override
    public Class<PropertiesName> type() {
        return PropertiesName.class;
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return "b";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
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
}
