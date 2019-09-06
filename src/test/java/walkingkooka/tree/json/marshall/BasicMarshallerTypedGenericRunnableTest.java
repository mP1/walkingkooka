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

package walkingkooka.tree.json.marshall;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;

public final class BasicMarshallerTypedGenericRunnableTest extends BasicMarshallerTestCase<BasicMarshallerTypedGenericRunnable>
        implements ToStringTesting<BasicMarshallerTypedGenericRunnable> {

    @Test
    public void testToString() {
        final BasicMarshallerTypedGeneric marshaller = BasicMarshallerTypedGeneric.with("test-type",
                TestJsonNodeValue::fromJsonNode,
                TestJsonNodeValue::toJsonNode,
                TestJsonNodeValue.class);
        this.toStringAndCheck(BasicMarshallerTypedGenericRunnable.with(marshaller), marshaller.toString());
    }

    @Override
    public Class<BasicMarshallerTypedGenericRunnable> type() {
        return BasicMarshallerTypedGenericRunnable.class;
    }
}