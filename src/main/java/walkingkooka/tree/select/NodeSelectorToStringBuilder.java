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

import walkingkooka.ShouldNeverHappenError;
import walkingkooka.build.Builder;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.naming.PathSeparator;

/**
 * Accepts the string representation of an axis, name and predicate node selector and builds up a {@link NodeSelector#toString()}.
 */
final class NodeSelectorToStringBuilder implements Builder<String> {

    static NodeSelectorToStringBuilder create() {
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

    void descendant(final PathSeparator separator) {
        this.separator = separator;
        this.commit();

        final char c = separator.character();
        this.b.append(c);
        this.b.append(c);
    }

    void self() {
        this.commit();
        this.appendSeparator(true);
        this.b.append('.');
    }

    void parent() {
        this.commit();
        this.appendSeparator(true);
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
        if(null!=this.axis) {
            this.commit();
        }
        this.axis = toString;
    }

    void node(final String toString) {
        if(null!=this.name) {
            this.commit();
        }
        this.name = toString;
    }

    void predicate(final String toString) {
        if(null!=this.predicate) {
            this.commit();
        }
        this.predicate = toString;
    }

    private void commit() {
        final String axis = this.axis;
        final String name = this.name;
        final String predicate = this.predicate;

        final int action = (null != axis ? 1 : 0) +
                (null != name ? 2 : 0) +
                (null != predicate ? 4 : 0);

        if(action > 0) {
            this.appendSeparator(false);
        }

        final StringBuilder b = this.b;
        switch(action) {
            case 0:
                break;
            case 1:
                b.append(axis).append("::*");
                this.axis = null;
                break;
            case 2:
                b.append(name);
                this.name = null;
                break;
            case 3:
                b.append(axis).append("::").append(name);
                this.axis = null;
                this.name = null;
                break;
            case 4:
                b.append("*[").append(predicate).append(']');
                this.predicate = null;
                break;
            case 5:
                b.append(axis).append("::*[").append(predicate).append(']');
                this.axis = null;
                this.predicate = null;
                break;
            case 6:
                b.append(name).append("[").append(predicate).append(']');
                this.name = null;
                this.predicate = null;
                break;
            case 7:
                b.append(axis).append("::").append(name).append("[").append(predicate).append(']');
                this.axis = null;
                this.name = null;
                this.predicate = null;
                break;
            default:
                throw new ShouldNeverHappenError(this.toString());
        }
    }

    private void appendSeparator(final boolean skipInitial) {
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
    private String name;
    private String predicate;
    private PathSeparator separator = PathSeparator.requiredAtStart('/');

    private final StringBuilder b = new StringBuilder();

    @Override
    public String toString() {
        return ToStringBuilder.create()
                .value(this.axis)
                .value(this.name)
                .value(this.predicate)
                .build();
    }
}
