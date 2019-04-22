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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodePointerTest implements ClassTesting2<NodePointer<JsonNode, JsonNodeName>>,
        ParseStringTesting<NodePointer<JsonNode, JsonNodeName>>,
        ToStringTesting<NodePointer<JsonNode, JsonNodeName>> {

    private final static JsonNodeName ABC = JsonNodeName.with("abc");
    private final static JsonNodeName DEF = JsonNodeName.with("def");
    private final static JsonNodeName GHI = JsonNodeName.with("ghi");
    private final static JsonNodeName JKL = JsonNodeName.with("jkl");

    private final static Function<String, JsonNodeName> NAME_FACTORY = (s -> JsonNodeName.with(s));

    private final static String TEXT = "text123";

    @Test
    public void testIndexInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.indexed(-1, JsonNode.class);
        });
    }

    @Test
    public void testIndexNullNodeClassFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.indexed(0, null);
        });
    }

    @Test
    public void testIndexInvalidIndexFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.indexed(0, JsonNode.class)
                    .indexed(-1);
        });
    }

    @Test
    public void testNamedNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.named(null, JsonNode.class);
        });
    }

    @Test
    public void testNamedNullNodeClassFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.named(ABC, null);
        });
    }

    @Test
    public void testNamedNullNameFails2() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.named(ABC, JsonNode.class)
                    .named(null);
        });
    }

    // toString.......................................................................................................

    @Test
    public void testToStringRelative() {
        this.toStringAndCheck(NodePointer.relative(1, JsonNode.class),
                "1");
    }

    @Test
    public void testToStringRelativeHash() {
        this.toStringAndCheck(NodePointer.relativeHash(1, JsonNode.class),
                "1#");
    }

    @Test
    public void testToStringRelativeIndex() {
        this.toStringAndCheck(NodePointer.relative(1, JsonNode.class)
                        .indexed(23),
                "1/23");
    }

    @Test
    public void testToStringRelativeNamed() {
        this.toStringAndCheck(NodePointer.relative(1, JsonNode.class)
                        .named(JsonNodeName.with("abc")),
                "1/abc");
    }

    @Test
    public void testToStringRelativeHashNamed() {
        this.toStringAndCheck(NodePointer.relativeHash(1, JsonNode.class)
                        .named(JsonNodeName.with("abc")),
                "1/abc");
    }

    @Test
    public void testToStringChildIndex() {
        this.toStringAndCheck(NodePointer.indexed(0, JsonNode.class),
                "/0");
    }

    @Test
    public void testToStringChildNamed() {
        this.toStringAndCheck("/abc",
                NodePointer.named(ABC, JsonNode.class)
                        .toString());
    }

    @Test
    public void testToStringChildNamedChildIndex() {
        this.toStringAndCheck(NodePointer.named(ABC, JsonNode.class)
                        .indexed(0),
                "/abc/0");
    }

    @Test
    public void testToStringChildNamedChildIndexX2() {
        this.toStringAndCheck(NodePointer.named(ABC, JsonNode.class)
                        .indexed(0)
                        .indexed(1),
                "/abc/0/1");
    }

    @Test
    public void testToStringChildIndexChildNamedChildIndex() {
        this.toStringAndCheck("/0/abc/1",
                NodePointer.indexed(0, JsonNode.class)
                        .named(ABC)
                        .indexed(1)
                        .toString());
    }

    @Test
    public void testToStringChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, JsonNode.class)
                        .named(DEF),
                "/abc/def");
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, JsonNode.class)
                        .named(DEF)
                        .named(GHI),
                "/abc/def/ghi");
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, JsonNode.class)
                        .named(DEF)
                        .named(GHI)
                        .named(JKL),
                "/abc/def/ghi/jkl");
    }

    @Test
    public void testToStringChildIndexChildIndexChildIndexChildIndex() {
        this.toStringAndCheck(NodePointer.indexed(0, JsonNode.class)
                        .indexed(1)
                        .indexed(2)
                        .indexed(3),
                "/0/1/2/3");
    }

    // traverse

    @Test
    public void testRelativeAbsent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.relative(1, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testRelativeSelf() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.relative(0, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeHashSelf() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.relative(0, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeParent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.relative(1, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonObjectNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, root.get(DEF).get(), root.toString());
    }

    @Test
    public void testNamedAbsent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(55, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(1, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndex() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(0, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode zero = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(zero);
        this.traverseAndCheck(pointer, root, zero.toString());
    }

    @Test
    public void testIndex2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(1, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode one = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.string("wrong"))
                .appendChild(one);
        this.traverseAndCheck(pointer, root, one.toString());
    }

    @Test
    public void testNamed() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode abc = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.object()
                .set(ABC, abc);
        this.traverseAndCheck(pointer, root, abc.toString());
    }

    @Test
    public void testNamed2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(DEF, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.string("wrong node"))
                .set(DEF, def);
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamed3() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(DEF);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamedLastAbsent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(DEF)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNamedLastAbsent2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(0, JsonNode.class)
                .indexed(1)
                .indexed(2);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(0, JsonNode.class)
                .indexed(99);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.array().appendChild(def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNestedArray() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(0, JsonNode.class)
                .indexed(1);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testIndexForObject() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.indexed(0, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, def);
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testIndexForObject2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.named(ABC, JsonNode.class)
                .indexed(0);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testRelativeNestedArray() {
        final NodePointer<JsonNode, JsonNodeName> pointer = NodePointer.relative(1, JsonNode.class)
                .indexed(2);
        this.checkIsRelative(pointer);

        final JsonNode text = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(JsonNode.string("wrong2")).appendChild(text));
        final JsonArrayNode rootArray = Cast.to(JsonArrayNode.class.cast(root).get(0));
        final JsonNode rootArrayElementOne = rootArray.get(1);


        this.traverseAndCheck(pointer, rootArrayElementOne, text.toString());
    }

    // parse.............................................................................................................

    @Override
    public void testParseEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.parse("/abc/-99", NAME_FACTORY, JsonNode.class);
        });
    }

    @Test
    public void testParseInvalidNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.parse("/abc//xyz", NAME_FACTORY, JsonNode.class);
        });
    }

    @Test
    public void testParseInvalidNameFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.parse("missing-leading-slash", NAME_FACTORY, JsonNode.class);
        });
    }

    @Test
    public void testParseNullNameFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.parse("/valid-pointer", null, JsonNode.class);
        });
    }

    @Test
    public void testParseNullNodeTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.parse("/valid-pointer", NAME_FACTORY, null);
        });
    }

    @Test
    public void testParseAppendWithNextFails() {
        this.parseFails("/-/1", UnsupportedOperationException.class);
    }

    @Test
    public void testParseThenTraverseElements() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/0/1");

        final JsonNode def = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamed() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/abc/def");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamedAndIndex() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/abc/0/def");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.array()
                        .appendChild(JsonNode.object().set(DEF, def).set(GHI, JsonNode.string("wrong!")))
                        .appendChild(JsonNode.number(123)));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseAppend() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/abc/-");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseThenTraverseAppend2() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/abc/def/-");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseTilde() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/tilde~0");

        final JsonNode text = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(JsonNodeName.with("tilde~"), text);
        this.traverseAndCheck(pointer, root, text.toString());
    }

    @Test
    public void testParseSlash() {
        final NodePointer<JsonNode, JsonNodeName> pointer = parse("/slash~1");

        final JsonNode text = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(JsonNodeName.with("slash/"), text);
        this.traverseAndCheck(pointer, root, text.toString());
    }

    private void traverseAndCheck(final NodePointer<JsonNode, JsonNodeName> pointer, final JsonNode root, final String toString) {
        final Optional<JsonNode> result = pointer.traverse(root);
        assertNotEquals(Optional.empty(), result, () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched a node but failed,\n" + root);
        assertEquals(toString, result.get().toString(), () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched the node\n" + root);
    }

    private void traverseFail(final NodePointer<JsonNode, JsonNodeName> pointer, final JsonNode root) {
        assertEquals(Optional.empty(), pointer.traverse(root), () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched nothing\n" + root);
    }

    private void checkIsAbsolute(final NodePointer<?, ?> pointer) {
        assertTrue(pointer.isAbsolute(), "isAbsolute");
        assertFalse(pointer.isRelative(), "isRelative");
        assertTrue(pointer.toString().startsWith("/"), () -> "pointer should start with '/' =" + pointer);
    }

    private void checkIsRelative(final NodePointer<?, ?> pointer) {
        assertFalse(pointer.isAbsolute(), "isAbsolute");
        assertTrue(pointer.isRelative(), "isRelative");
        assertFalse(pointer.toString().startsWith("/"), () -> "pointer shouldnt start with '/' =" + pointer);
    }

    @Override
    public Class<NodePointer<JsonNode, JsonNodeName>> type() {
        return Cast.to(NodePointer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public NodePointer<JsonNode, JsonNodeName> parse(final String pointer) {
        final NodePointer<JsonNode, JsonNodeName> parsed = NodePointer.parse(pointer, NAME_FACTORY, JsonNode.class);
        assertEquals(pointer,
                parsed.toString(),
                () -> "pointer.parse: " + CharSequences.quoteAndEscape(pointer));
        return parsed;
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }
}
