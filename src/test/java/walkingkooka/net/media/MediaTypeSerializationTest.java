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

package walkingkooka.net.media;

import org.junit.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.SerializationTestCase;

import java.util.Map;

import static org.junit.Assert.assertEquals;

final public class MediaTypeSerializationTest extends SerializationTestCase<MediaType> {

    @Test
    public void testConstantsAreSingletons() throws Exception {
        this.constantsAreSingletons();
    }

    @Test
    public void testTwoParameters() throws Exception {
        final String raw = "a/b;x=1;y=2";
        final MediaType mimeType = this.cloneUsingSerialization(MediaType.parse(raw));
        assertEquals("primary", "a", mimeType.type());
        assertEquals("sub", "b", mimeType.subType());
        assertEquals("value", "a/b", mimeType.value());

        final Map<MediaTypeParameterName, String> parameters = Maps.ordered();
        parameters.put(MediaTypeParameterName.with("x"), "1");
        parameters.put(MediaTypeParameterName.with("y"), "2");
        assertEquals("parameters", parameters, mimeType.parameters());
    }

    @Override
    protected MediaType create() {
        return MediaType.parse("custom/bin;parameter123=value456");
    }

    @Override
    protected boolean isSingleton() {
        return false;
    }

    @Override
    protected Class<MediaType> type() {
        return MediaType.class;
    }
}
