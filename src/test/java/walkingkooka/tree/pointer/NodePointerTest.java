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
import walkingkooka.naming.Name;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ParseStringTesting;
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

public final class NodePointerTest extends ClassTestCase<NodePointer<JsonNode, JsonNodeName, Name, Object>>
        implements ParseStringTesting<NodePointer<JsonNode, JsonNodeName, Name, Object>> {

    private final static JsonNodeName ABC = JsonNodeName.with("abc");
    private final static JsonNodeName DEF = JsonNodeName.with("def");
    private final static JsonNodeName GHI = JsonNodeName.with("ghi");
    private final static JsonNodeName JKL = JsonNodeName.with("jkl");
    private final static JsonNodeName TILDE = JsonNodeName.with("tilde~0");
    private final static JsonNodeName SLASH = JsonNodeName.with("slash~1");
    private final static Function<String, JsonNodeName> NAME_FACTORY = (s -> JsonNodeName.with(s));

    private final static String TEXT = "text123";

    @Test
    public void testIndexInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.index(-1, JsonNode.class);
        });
    }

    @Test
    public void testIndexNullNodeClassFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePointer.index(0, null);
        });
    }

    @Test
    public void testIndexInvalidIndexFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodePointer.index(0, JsonNode.class)
                    .index(-1);
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
        assertEquals("1",
                NodePointer.relative(1, JsonNode.class)
                        .toString());
    }

    @Test
    public void testToStringRelativeHash() {
        assertEquals("1#",
                NodePointer.relativeHash(1, JsonNode.class)
                        .toString());
    }

    @Test
    public void testToStringRelativeIndex() {
        assertEquals("1/23",
                NodePointer.relative(1, JsonNode.class)
                        .index(23)
                        .toString());
    }

    @Test
    public void testToStringRelativeNamed() {
        assertEquals("1/abc",
                NodePointer.relative(1, JsonNode.class)
                        .named(JsonNodeName.with("abc"))
                        .toString());
    }

    @Test
    public void testToStringRelativeHashNamed() {
        assertEquals("1/abc",
                NodePointer.relativeHash(1, JsonNode.class)
                        .named(JsonNodeName.with("abc"))
                        .toString());
    }

    @Test
    public void testToStringChildIndex() {
        assertEquals("/0",
                NodePointer.index(0, JsonNode.class)
                .toString());
    }

    @Test
    public void testToStringChildNamed() {
        assertEquals("/abc",
                NodePointer.named(ABC, JsonNode.class)
                .toString());
    }

    @Test
    public void testToStringChildNamedChildIndex() {
        assertEquals("/abc/0",
                NodePointer.named(ABC, JsonNode.class)
                .index(0)
                .toString());
    }

    @Test
    public void testToStringChildNamedChildIndexX2() {
        assertEquals("/abc/0/1",
                NodePointer.named(ABC, JsonNode.class)
                .index(0)
                .index(1)
                .toString());
    }

    @Test
    public void testToStringChildIndexChildNamedChildIndex() {
        assertEquals("/0/abc/1",
                NodePointer.index(0, JsonNode.class)
                .named(ABC)
                .index(1)
                .toString());
    }

    @Test
    public void testToStringChildNamedChildNamed() {
        assertEquals("/abc/def",
                NodePointer.named(ABC, JsonNode.class)
                .named(DEF)
                .toString());
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamed() {
        assertEquals("/abc/def/ghi",
                NodePointer.named(ABC, JsonNode.class)
                .named(DEF)
                .named(GHI)
                .toString());
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamedChildNamed() {
        assertEquals("/abc/def/ghi/jkl",
                NodePointer.named(ABC, JsonNode.class)
                        .named(DEF)
                        .named(GHI)
                        .named(JKL)
                        .toString());
    }

    @Test
    public void testToStringChildIndexChildIndexChildIndexChildIndex() {
        assertEquals("/0/1/2/3",
                NodePointer.index(0, JsonNode.class)
                        .index(1)
                        .index(2)
                        .index(3)
                        .toString());
    }

    // traverse

    @Test
    public void testRelativeAbsent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.relative(1, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testRelativeSelf(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.relative(0, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeHashSelf(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.relative(0, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeParent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.relative(1, JsonNode.class);
        this.checkIsRelative(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseAndCheck(pointer, ((JsonObjectNode) root).get(DEF).get(), root.toString());
    }

    @Test
    public void testNamedAbsent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(55, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(DEF, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(1, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.string(TEXT));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndex(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(0, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode zero = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(zero);
        this.traverseAndCheck(pointer, root, zero.toString());
    }

    @Test
    public void testIndex2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(1, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode one = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.string("wrong"))
                .appendChild(one);
        this.traverseAndCheck(pointer, root, one.toString());
    }

    @Test
    public void testNamed(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode abc = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.object()
                .set(ABC, abc);
        this.traverseAndCheck(pointer, root, abc.toString());
    }

    @Test
    public void testNamed2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(DEF, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.string("wrong node"))
                .set(DEF, def);
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamed3(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(DEF);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamedLastAbsent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(DEF)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNamedLastAbsent2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(0, JsonNode.class)
                .index(1)
                .index(2);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(0, JsonNode.class)
                .index(99);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.array().appendChild(def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNestedArray(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(0, JsonNode.class)
                .index(1);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testIndexForObject(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.index(0, JsonNode.class);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, def);
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testIndexForObject2(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.named(ABC, JsonNode.class)
                .index(0);
        this.checkIsAbsolute(pointer);

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testRelativeNestedArray(){
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = NodePointer.relative(1, JsonNode.class)
                .index(2);
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
    public void testParseThenTraverseElements() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/0/1");

        final JsonNode def = JsonNode.string(TEXT);
        final JsonNode root = JsonNode.array()
                .appendChild(JsonNode.array().appendChild(JsonNode.string("wrong")).appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamed() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/abc/def");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamedAndIndex() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/abc/0/def");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.array()
                        .appendChild(JsonNode.object().set(DEF, def).set(GHI, JsonNode.string("wrong!")))
                        .appendChild(JsonNode.number(123)));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNone() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/abc/-");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseThenTraverseNone2() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/-/def");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseThenTraverseNone3() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/abc/def/-");

        final JsonNode def = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(ABC, JsonNode.object().set(DEF, def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseTilde() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/tilde~0");

        final JsonNode text = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(JsonNodeName.with("tilde~"), text);
        this.traverseAndCheck(pointer, root, text.toString());
    }

    @Test
    public void testParseSlash() {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer = parse("/slash~1");

        final JsonNode text = JsonNode.string(TEXT);

        final JsonNode root = JsonNode.object()
                .set(JsonNodeName.with("slash/"), text);
        this.traverseAndCheck(pointer, root, text.toString());
    }

    private void traverseAndCheck(final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer, final JsonNode root, final String toString) {
        final Optional<JsonNode> result = pointer.traverse(root);
        assertNotEquals(Optional.empty(), result, () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched a node but failed,\n" + root);
        assertEquals(toString, result.get().toString(), () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched the node\n" + root);
    }

    private void traverseFail(final NodePointer<JsonNode, JsonNodeName, Name, Object> pointer, final JsonNode root) {
        assertEquals(Optional.empty(), pointer.traverse(root), () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched nothing\n" + root);
    }

    private void checkIsAbsolute(final NodePointer<?, ?, ?, ?> pointer) {
        assertTrue(pointer.isAbsolute(), "isAbsolute");
        assertFalse(pointer.isRelative(), "isRelative");
        assertTrue(pointer.toString().startsWith("/"), () -> "pointer should start with '/' =" + pointer);
    }

    private void checkIsRelative(final NodePointer<?, ?, ?, ?> pointer) {
        assertFalse(pointer.isAbsolute(), "isAbsolute");
        assertTrue(pointer.isRelative(), "isRelative");
        assertFalse(pointer.toString().startsWith("/"), () -> "pointer shouldnt start with '/' =" + pointer);
    }

    @Override
    protected Class<NodePointer<JsonNode, JsonNodeName, Name, Object>> type() {
        return Cast.to(NodePointer.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public NodePointer<JsonNode, JsonNodeName, Name, Object> parse(final String pointer) {
        final NodePointer<JsonNode, JsonNodeName, Name, Object> parsed =  NodePointer.parse(pointer, NAME_FACTORY, JsonNode.class);
        assertEquals(pointer, parsed.toString(), "pointer.toString");
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
