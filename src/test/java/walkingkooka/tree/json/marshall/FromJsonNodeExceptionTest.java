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
import walkingkooka.test.StandardThrowableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FromJsonNodeExceptionTest implements StandardThrowableTesting<FromJsonNodeException> {


    @Test
    public void testWithNullMessageAndNodeFails() {
        this.withMessageAndNodeFails(null, node());
    }

    private void withMessageAndNodeFails(final String message, final JsonNode node) {
        assertThrows(NullPointerException.class, () -> {
            new FromJsonNodeException(message, node);
        });
    }

    @Test
    public void testWithMessageAndNode() {
        final FromJsonNodeException exception = this.createThrowable(MESSAGE);
        checkThrowable(exception, MESSAGE, null);
        checkNode(exception);
    }

    @Test
    public void testWithMessageAndNodeAndCause() {
        final FromJsonNodeException exception = this.createThrowable(MESSAGE, CAUSE);
        checkThrowable(exception, MESSAGE, CAUSE);
        checkNode(exception);
    }

    @Override
    public void testWithEmptyMessageAndNonNullCauseFails() {

    }

    @Override
    public void testWithNullMessageAndCauseExceptionFails() {

    }

    @Test
    public void testWithNullMessageNodeAndCause() {
        final FromJsonNodeException exception = new FromJsonNodeException(null, this.node(), CAUSE);
        checkThrowable(exception, FromJsonNodeException.DEFAULT_MESSAGE, CAUSE);
        checkNode(exception);
    }

    @Test
    public void testWithEmptyMessageNodeAndCause() {
        final FromJsonNodeException exception = new FromJsonNodeException("", this.node(), CAUSE);
        checkThrowable(exception, FromJsonNodeException.DEFAULT_MESSAGE, CAUSE);
        checkNode(exception);
    }

    private void checkNode(final FromJsonNodeException exception) {
        assertEquals(this.node(), exception.node(), "node");
    }

    private JsonNode node() {
        return JsonNode.string("abc123");
    }

    @Override
    public FromJsonNodeException createThrowable(final String message) {
        return new FromJsonNodeException(message, node());
    }

    @Override
    public FromJsonNodeException createThrowable(final String message, final Throwable cause) {
        return new FromJsonNodeException(message, node(), cause);
    }

    @Override
    public Class<FromJsonNodeException> type() {
        return FromJsonNodeException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
