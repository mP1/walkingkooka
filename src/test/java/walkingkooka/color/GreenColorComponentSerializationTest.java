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

public final class GreenColorComponentSerializationTest extends SerializationTestCase<GreenColorComponent> {

    @Test
    public void testFF() throws Exception {
        this.cloneUsingSerialization(GreenColorComponent.with((byte) 255));
    }

    @Override
    protected Class<GreenColorComponent> type() {
        return GreenColorComponent.class;
    }

    @Override
    protected GreenColorComponent create() {
        return GreenColorComponent.with((byte) 123);
    }

    @Override
    protected boolean isSingleton() {
        return true;
    }
}
