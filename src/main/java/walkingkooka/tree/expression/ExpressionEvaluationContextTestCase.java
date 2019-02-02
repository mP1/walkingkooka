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

import org.junit.jupiter.api.Test;
import walkingkooka.ContextTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ExpressionEvaluationContextTestCase<C extends ExpressionEvaluationContext> extends ContextTestCase<C> {

    @Test
    public void testFunctionNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().function(null, ExpressionEvaluationContext.NO_PARAMETERS);
        });
    }

    @Test
    public void testFunctionNullParametersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().function(ExpressionNodeName.with("sum"), null);
        });
    }

    @Test
    public void testReferenceNullReferenceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().reference(null);
        });
    }

    @Test
    public void testConvertNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().convert(null, Object.class);
        });
    }

    @Test
    public void testConvertNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().convert("value", null);
        });
    }

    protected void toValueAndCheck(final ExpressionNode node, final ExpressionEvaluationContext context, final Object value) {
        assertEquals(value,
                node.toValue(context),
                ()-> "ExpressionNode.toValue failed, node=" + node + " context=" + context);
    }

    @Override
    protected String requiredNameSuffix() {
        return ExpressionEvaluationContext.class.getSimpleName();
    }
}
