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

package walkingkooka.tree.xml;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class XmlAttributeNameTest implements ClassTesting2<XmlAttributeName>,
        NameTesting<XmlAttributeName, XmlAttributeName> {

    @Test
    public void testWithNoPrefixNullFails() {
        assertThrows(NullPointerException.class, () -> {
            XmlAttributeName.with("a", null);
        });
    }

    @Test
    public void testDifferentPrefix() {
        this.checkNotEquals(this.createName(this.nameText(), "different"));
    }

    @Test
    public void testDifferentPrefix2() {
        final String nameText = this.nameText();

        this.checkNotEquals(this.createName(nameText, "prefix"),
                this.createName(nameText, "different"));
    }

    // Comparable.......................................................................................................

    @Test
    public void testCompareToArraySort() {
        final XmlAttributeName a1 = XmlAttributeName.with("A1", XmlAttributeName.NO_PREFIX);
        final XmlAttributeName b2 = XmlAttributeName.with("B2", XmlAttributeName.NO_PREFIX);
        final XmlAttributeName c3 = XmlAttributeName.with("c3", Optional.of(XmlNameSpacePrefix.with("A")));
        final XmlAttributeName d4 = XmlAttributeName.with("d4", XmlAttributeName.NO_PREFIX);

        this.compareToArraySortAndCheck(d4, a1, c3, b2,
                a1, b2, c3, d4);
    }

    private XmlAttributeName createName(final String name, final String prefix) {
        return XmlAttributeName.with(name, Optional.of(XmlNameSpacePrefix.with(prefix)));
    }

    @Override
    public XmlAttributeName createName(final String name) {
        return XmlAttributeName.with(name, XmlAttributeName.NO_PREFIX);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "attribute-22";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "attribute-1";
    }

    @Override
    public Class<XmlAttributeName> type() {
        return XmlAttributeName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
