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

package walkingkooka.tree.expression;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTestCase2;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertNotSame;

public abstract class ExpressionNodeTestCase<N extends ExpressionNode> extends NodeTestCase2<ExpressionNode, ExpressionNodeName, Name, Object> {

    @Test
    public final void testCheckNaming() {
        this.checkNamingStartAndEnd("Expression", Node.class);
    }

    @Test
    @Ignore
    public final void testSetSameAttributes() {
        // Ignored
    }

    @Test
    public final void testIsMethods() throws Exception {
        final String prefix = "Expression";
        final String suffix = Node.class.getSimpleName();

        final N node = this.createExpressionNode();
        final String name = node.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for(Method method : node.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isRoot")){
                continue;
            }
            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(node));
        }
    }

    @Override
    protected ExpressionNode createNode() {
        return this.createExpressionNode();
    }

    abstract N createExpressionNode();

    @Override
    protected Class<ExpressionNode> type() {
        return Cast.to(this.expressionNodeType());
    }

    abstract Class<N> expressionNodeType();

    @Override
    protected ExpressionNode appendChildAndCheck(final ExpressionNode parent, final ExpressionNode child) {
        final N newParent = parent.appendChild(child).cast();
        assertNotSame("appendChild must not return the same node", newParent, parent);

        final List<N> children = Cast.to(newParent.children());
        assertNotEquals("children must have at least 1 child", 0, children.size());
        //assertEquals("last child must be the added child", child.name(), children.get(children.size() - 1).name());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    static ExpressionNode text(final String text) {
        return ExpressionNode.text(text);
    }
}
