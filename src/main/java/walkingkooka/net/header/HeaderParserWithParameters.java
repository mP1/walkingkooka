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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;

import java.util.Map;

/**
 * A {@link HeaderParser} variant that supports a single value followed by optional parameters.
 */
abstract class HeaderParserWithParameters<V extends HeaderValueWithParameters<N>,
        N extends HeaderParameterName<?>>
        extends HeaderParser {
    /**
     * Package private to limit sub classing.
     */
    HeaderParserWithParameters(String text) {
        super(text);
    }

    @Override
    final void whitespace() {
        this.whitespace0();
    }

    @Override
    final void tokenSeparator() {
        if(this.requireParameterName || null!=this.parameterName && null == this.parameterValue) {
            this.failInvalidCharacter();
        }
        this.requireParameterName = true;
        this.parameterName = null;
        this.parameterValue = null;
    }

    @Override
    final void keyValueSeparator() {
        if(null == this.value || null == this.parameterName) {
            this.failInvalidCharacter();
        }
    }

    @Override
    final void multiValueSeparator() {
        if(! this.allowMultipleValues() || null == this.value) {
            this.failInvalidCharacter();
        }
        this.maybeEndOfValue();

        this.value = null;
        this.requireValue = true;
    }

    /**
     * Parsers that are attempting to parse one or more values should return true.
     */
    abstract boolean allowMultipleValues();

    /**
     * Assumes wildcard maps to a value. Parameters may follow.
     */
    final void wildcard() {
        if(this.requireValue) {
            this.saveValue(this.wildcardValue());
        } else {
            this.failInvalidCharacter();
        }
    }

    abstract V wildcardValue();

    /**
     * Slashes are a failure.
     */
    @Override
    final void slash() {
       this.failInvalidCharacter();
    }

    /**
     * Handles parsing a token.
     */
    @Override
    final void token() {
        if(this.requireValue) {
            this.saveValue(this.value());
        } else {
            final N parameterName = this.parameterName;
            if(this.requireParameterName) {
                this.parameterName = this.parameterName();
                this.requireParameterName = false;
            } else {
                if(null != this.parameterName && null == this.parameterValue) {
                    this.addParameterText(this.unquotedParameterValue(parameterName));
                } else {
                    this.failInvalidCharacter();
                }
            }
        }
    }

    abstract V value();

    private void saveValue(final V value) {
        this.value = value;
        this.parameters = Maps.ordered();
        this.requireValue = false;
        this.parameterName = null;
        this.parameterValue =null;
    }

    abstract N parameterName();

    @Override
    final void quotedText() {
        final N parameterName = this.parameterName;
        if(null==parameterName || null != this.parameterValue) {
            this.failInvalidCharacter();
        }
        this.addParameterText(this.quotedParameterValue(parameterName));
    }

    abstract String quotedParameterValue(final N parameterName);

    abstract String unquotedParameterValue(final N parameterName);

    /**
     * Called when the end of text is reached, allow opportunities to complete key/value pairs or fail etc.
     */
    @Override
    final void endOfText() {
        if(this.requireValue) {
            this.missingValue();
        }
        if(null!=this.parameterName && null == this.parameterValue) {
            this.failMissingParameterValue();
        }
        this.maybeEndOfValue();
    }

    final void maybeEndOfValue() {
        if(null!=value) {
            if(null!=this.parameterName && null == this.parameterValue) {
                this.failMissingParameterValue();
            }
            this.valueComplete(Cast.to(this.value.setParameters(this.parameters)));
        }
    }

    /**
     * Called with complete values.
     */
    abstract void valueComplete(final V value);

    /**
     * When true indicates a value expected after a {@link #multiValueSeparator()}.
     */
    private boolean requireValue = true;

    /**
     * The value
     */
    private V value;

    /**
     * Becomes true after encountering a {@link #tokenSeparator()}.
     */
    private boolean requireParameterName;

    /**
     * When present indicates a parameter value is expected next.
     */
    private N parameterName;

    /**
     * The last parameter value.
     */
    private Object parameterValue;

    /**
     * Adds a new parameter value after converting the text to a value.
     */
    private void addParameterText(final String text) {
        this.addParameterValue(this.parameterName.toValue(text));
    }

    /**
     * Adds a new parameter value.
     */
    private void addParameterValue(final Object value) {
        final N parameterName = this.parameterName;
        this.parameterValue = parameterName.checkValue(value);
        this.parameters.put(parameterName, this.parameterValue);
    }

    /**
     * After a value is encountered a map that fills with parameters.
     */
    private Map<N, Object> parameters;
}
