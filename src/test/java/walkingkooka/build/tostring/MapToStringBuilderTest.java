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

package walkingkooka.build.tostring;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MapToStringBuilderTest extends VectorToStringBuilderTestCase<Map<String, Integer>> {

    @Test
    public void testLabelValue() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator("$");
        b.label(LABEL);
        b.value(Maps.of("A", "1", "B", "2", "C", "3"));

        this.buildAndCheck(b, LABEL + "$A$1, B$2, C$3");
    }

    @Test
    public void testValueList() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator("$");
        b.label(LABEL);
        b.value(Maps.of("A", Lists.of(1, 2), "B", Lists.of(3, 4)));

        this.buildAndCheck(b, LABEL + "$A$1, 2, B$3, 4");
    }

    @Test
    public void testQuoted() {
        final ToStringBuilder b = this.builder();
        b.enable(ToStringBuilderOption.QUOTE);
        b.value(Maps.of("A", "1", "B", "2", "C", 333));

        this.buildAndCheck(b, "A=\"1\", B=\"2\", C=333");
    }

    @Test
    public void testLabelSeparator() {
        final ToStringBuilder b = this.builder();
        b.labelSeparator("$");
        b.value(Maps.of("A", "1", "B", "2", "C", "3"));

        this.buildAndCheck(b, "A$1, B$2, C$3");
    }

    @Test
    public void testSeparator() {
        final Map<String, String> map = Maps.of("A", "1", "B", "2", "C", "3");

        final ToStringBuilder b = this.builder();
        b.separator("$");
        b.value(map);
        b.value(map);

        this.buildAndCheck(b, "A=1, B=2, C=3$A=1, B=2, C=3");
    }

    @Override
    Map<String, Integer> defaultValue() {
        return Maps.ordered();
    }

    @Override
    Map<String, Integer> value1() {
        return Maps.of("ABC", 123);
    }

    @Override
    Map<String, Integer> value2() {
        return Maps.of("A", 1, "B", 2, "C", 3);
    }

    @Override
    void append(final ToStringBuilder builder, final Map<String, Integer> value) {
        builder.append(value);
    }

    @Override
    void value(final ToStringBuilder builder, final Map<String, Integer> value) {
        builder.value(value);
    }

    @Override
    String value1ToString() {
        return "ABC=123";
    }

    @Override
    String value2ToString(final String separator) {
        assertEquals(ToStringBuilder.LABEL_SEPARATOR, "=");
        return "A=1" + separator + "B=2" + separator + "C=3";
    }
}
