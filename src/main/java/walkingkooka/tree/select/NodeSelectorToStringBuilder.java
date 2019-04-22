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

package walkingkooka.tree.select;

import walkingkooka.NeverError;
import walkingkooka.build.Builder;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.naming.PathSeparator;

/**
 * Accepts the string representation of an axis, node and predicate node selector and builds up a {@link NodeSelector#toString()}.
 */
final class NodeSelectorToStringBuilder implements Builder<String> {

    static NodeSelectorToStringBuilder empty() {
        return new NodeSelectorToStringBuilder();
    }

    private NodeSelectorToStringBuilder() {
        super();
    }

    void absolute(final PathSeparator separator) {
        this.separator = separator;
        this.commit();
        this.b.append(separator.character());
    }

    void descendantOrSelf(final PathSeparator separator) {
        this.separator = separator;
        this.commit();

        final char c = separator.character();
        this.b.append(c);
        this.b.append(c);
    }

    void self() {
        this.commit();
        this.appendSeparator();
        this.b.append('.');
    }

    void parent() {
        this.commit();
        this.appendSeparator();
        this.b.append("..");
    }

    void separator(final PathSeparator separator) {
        this.separator = separator;
    }

    void append(final String toString) {
        this.commit();
        this.b.append(toString);
    }

    void axis(final String toString) {
        // cant combine two axis.
        if (null != this.axis) {
            this.commit();
        }
        // output this step with the given axis, and start afresh with any next axis/node/predicate additions.
        this.axis = toString;
        this.commit();
    }

    void node(final String toString) {
        if (null != this.node) {
            this.commit();
        }
        this.node = toString;
    }

    void predicate(final String toString) {
        if (null != this.predicate) {
            this.commit();
        }
        this.predicate = toString;
    }

    private void commit() {
        final String axis = this.axis;
        final String node = this.node;
        final String predicate = this.predicate;

        final int action = (null != axis ? 1 : 0) +
                (null != node ? 2 : 0) +
                (null != predicate ? 4 : 0);

        if (action > 0) {
            this.appendSeparator();
        }

        final StringBuilder b = this.b;
        switch (action) {
            case 0:
                break;
            case 1: // axis
                b.append(axis).append("::*");
                this.axis = null;
                break;
            case 2: // node
                b.append(node);
                this.node = null;
                break;
            case 3: // axis node
                b.append(axis).append("::").append(node);
                this.axis = null;
                this.node = null;
                break;
            case 4: // predicate
                b.append("*[").append(predicate).append(']');
                this.predicate = null;
                break;
            case 5: // axis :: wildcard predicate
                b.append(axis).append("::*[").append(predicate).append(']');
                this.axis = null;
                this.predicate = null;
                break;
            case 6: // node predicate
                b.append(node).append("[").append(predicate).append(']');
                this.node = null;
                this.predicate = null;
                break;
            case 7: // axis node predicate
                b.append(axis).append("::").append(node).append("[").append(predicate).append(']');
                this.axis = null;
                this.node = null;
                this.predicate = null;
                break;
            default:
                NeverError.unhandledCase(action, 0, 1, 2, 3, 4, 5, 6, 7);
        }
    }

    private void appendSeparator() {
        final StringBuilder b = this.b;

        final PathSeparator separator = this.separator;
        final int length = b.length();
        if (length > 0) {
            final char c = separator.character();
            if (c != b.charAt(length - 1)) {
                b.append(c);
            }

        }
    }

    @Override
    public String build() {
        this.commit();
        return b.toString();
    }

    private String axis;
    private String node;
    private String predicate;
    private PathSeparator separator = PathSeparator.requiredAtStart('/');

    private final StringBuilder b = new StringBuilder();

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.axis)
                .value(this.node)
                .value(this.predicate)
                .build();
    }
}
