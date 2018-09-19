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

package walkingkooka.color;

import org.junit.Test;
import walkingkooka.test.SerializationTestCase;

public final class BlueColorComponentSerializationTest extends SerializationTestCase<BlueColorComponent> {

    @Test
    public void testFF() throws Exception {
        this.cloneUsingSerialization(BlueColorComponent.with((byte) 255));
    }

    @Override
    protected Class<BlueColorComponent> type() {
        return BlueColorComponent.class;
    }

    @Override
    protected BlueColorComponent create() {
        return BlueColorComponent.with((byte) 123);
    }

    @Override
    protected boolean isSingleton() {
        return true;
    }
}
