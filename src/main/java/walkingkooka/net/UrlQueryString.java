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

package walkingkooka.net;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The query string component within a {@link Url}. Methods are available to retrieve the first parameter value, or all parameter values
 * or to view all parameters as a {@link Map}.
 */
public final class UrlQueryString
        implements Value<String>, HashCodeEqualsDefined, Serializable {

    /**
     * An empty {@link UrlQueryString} with no length or parameters.
     */
    public final static UrlQueryString EMPTY = new UrlQueryString("", Lists.empty(), Maps.empty());

    private final static List<UrlParameterKeyValuePair> ENTRIES_ABSENT = null;
    private final static Map<UrlParameterName, UrlParameterValueList> PARAMETERS_MAP_ABSENT = null;

    /**
     * Factory that creates a new {@link UrlQueryString}
     */
    public static UrlQueryString with(final String value) {
        Objects.requireNonNull(value, "queryString");

        return value.length() == 0 ?
                UrlQueryString.EMPTY :
                new UrlQueryString(value, ENTRIES_ABSENT, PARAMETERS_MAP_ABSENT);
    }

    /**
     * Private constructor use factory
     */
    private UrlQueryString(final String queryString, final List<UrlParameterKeyValuePair> pairs, final Map<UrlParameterName, UrlParameterValueList> parameters) {
        super();
        this.queryString = queryString;
        this.pairs = pairs;
        this.parameters = parameters;
    }

    /**
     * Returns the raw query string.
     */
    @Override
    public String value() {
        return this.queryString;
    }

    private final String queryString;

    // parameters ................................................................................................

    /**
     * Returns a read-only {@link Map} holding all the parameters and values.
     */
    public Map<UrlParameterName, List<String>> parameters() {
        this.parseQueryStringIfNecessary();
        return Cast.to(this.parameters);
    }

    /**
     * Lazily created map.
     */
    private transient Map<UrlParameterName, UrlParameterValueList> parameters;

    /**
     * Makes a copy of all parameters.
     */
    private Map<UrlParameterName, UrlParameterValueList> parametersCopy() {
        final Map<UrlParameterName, UrlParameterValueList> copy = Maps.ordered();
        copy.putAll(this.parameters);
        return copy;
    }

    /**
     * Retrieves the parameter with the name returning the first value.
     */
    public Optional<String> parameter(final UrlParameterName name) {
        this.parseQueryStringIfNecessary();

        Optional<String> value = Optional.empty();

        for(UrlParameterKeyValuePair pair : this.pairs){
            if(pair.name.equals(name)){
                value = Optional.ofNullable(pair.value);
                break;
            }
        }

        return value;
    }

    /**
     * Retrieves the parameter with the name returning all values or an empty list.
     */
    public List<String> parameterValues(final UrlParameterName name) {
        return this.parameters().getOrDefault(name, NO_VALUES);
    }

    private final static List<String> NO_VALUES = Lists.empty();

    /**
     * Lazily parses the query string which initializes the map and list of pairs.
     */
    private void parseQueryStringIfNecessary() {
        if(null==this.parameters){
            this.parseQueryString();
        }
    }

    /**
     * Parses the query string and creates a map of decoded query parameters and key/value pairs.
     */
    private void parseQueryString(){
        final List<UrlParameterKeyValuePair> pairs = Lists.array();
        final Map<UrlParameterName, UrlParameterValueList> parameters = Maps.ordered();

        final char paramSeparator = Url.QUERY_PARAMETER_SEPARATOR.character();
        final char paramSeparator2 = Url.QUERY_PARAMETER_SEPARATOR2.character();
        final String queryString = this.queryString;

        // parse query
        final int length = queryString.length();
        int start = 0;

        for (int i = 0; i < length; i++) {
            final char c = queryString.charAt(i);

            // end of name/value pair
            if (paramSeparator == c || paramSeparator2 == c) {
                final UrlParameterKeyValuePair pair = UrlParameterKeyValuePair.encodedWithSeparator(queryString.substring(start, i) ,c);
                add(pair, parameters);
                pairs.add(pair);
                start = i + 1;
            }
        }

        if(start < length) {
            final UrlParameterKeyValuePair pair = UrlParameterKeyValuePair.encodedWithoutSeparator(queryString.substring(start, length));
            add(pair, parameters);
            pairs.add(pair);
        }

        this.parameters = Maps.readOnly(parameters);
        this.pairs = pairs;
    }

    /**
     * Adds values to the multimap of values.
     */
    private static void add(final UrlParameterKeyValuePair pair, final Map<UrlParameterName, UrlParameterValueList> parameters) {
        final UrlParameterName name = pair.name;
        UrlParameterValueList values = parameters.get(name);
        if(null==values){
            values = UrlParameterValueList.empty();
            parameters.put(name, values);
        }
        values.addParameterValue(pair.value);
    }

    /**
     * Adds a new parameter to this query string.
     */
    public UrlQueryString addParameter(final UrlParameterName name, final String value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        return this.queryString.isEmpty() ?
               this.addParameter0(name, value) :
               this.addParameter1(name, value);
    }

    private UrlQueryString addParameter0(final UrlParameterName name, final String value) {
        final UrlParameterValueList values = UrlParameterValueList.empty();
        values.addParameterValue(value);

        return new UrlQueryString(encode(name, value),
            Lists.of(UrlParameterKeyValuePair.nameAndValue(name, value)),
            Maps.of(name, values));
    }

    private UrlQueryString addParameter1(final UrlParameterName name, final String value) {
        this.parseQueryStringIfNecessary();

        final UrlParameterKeyValuePair pair = UrlParameterKeyValuePair.nameAndValue(name, value);

        final List<UrlParameterKeyValuePair> pairs = this.pairsCopy();
        pairs.add(pair);

        final Map<UrlParameterName, UrlParameterValueList> parameters = this.parametersCopy();
        add(pair, parameters);

        // need to find the last param separator and append to query string if necessary and then append new param.
        final char paramSeparator = Url.QUERY_PARAMETER_SEPARATOR.character();
        final char paramSeparator2 = Url.QUERY_PARAMETER_SEPARATOR2.character();
        String queryString = this.queryString;

        final int length = queryString.length();
        final char last = queryString.charAt(length - 1);

        // queryString already ends in separator just append new key/value.
        if(paramSeparator == last || paramSeparator2 == last){
            queryString = queryString + encode(name, value);
        } else {
            char lastParamSeparator = paramSeparator;

            // find the last separator and copy that
            for(int i = 1; i < length; i++) {
                final char c = queryString.charAt(length - i - 1);
                if(paramSeparator == c || paramSeparator2 == c){
                    lastParamSeparator = c;
                    break;
                }
            }
            queryString = queryString + lastParamSeparator + encode(name, value);
        }

        return new UrlQueryString(queryString, pairs, parameters);
    }

    private static String encode(final UrlParameterName name, final String value) {
        return  encode(name.value()) + Url.QUERY_NAME_VALUE_SEPARATOR.character() + encode(value);
    }

    private static String encode(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException never) {
            throw new Error(never);
        }
    }

    /**
     * Removes all parameter values with the given name.
     */
    public UrlQueryString removeParameter(final UrlParameterName name) {
        Objects.requireNonNull(name, "name");

        return this.removeParameter0(name, null);
    }

    /**
     * Removes all parameter values with the given name and value
     */
    public UrlQueryString removeParameter(final UrlParameterName name, final String value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        return this.removeParameter0(name, value);
    }

    private UrlQueryString removeParameter0(final UrlParameterName name, final String value) {
        this.parseQueryStringIfNecessary();

        final List<UrlParameterKeyValuePair> pairs = this.pairsCopy();
        boolean removed = false;
        final StringBuilder queryString = new StringBuilder();

        char separator = 0;

        for(final Iterator<UrlParameterKeyValuePair> i = pairs.iterator(); i.hasNext();){
            final UrlParameterKeyValuePair pair = i.next();
            if(pair.name.equals(name) && (null == value || value.equals(pair.value))) {
                i.remove();
                removed = true;
            } else {
                if(0 != separator){
                    queryString.append(separator);
                }
                queryString.append(pair.encoded);
                if(pair.separatorIncluded) {
                    separator = pair.separator;
                }
            }
        }

        return removed ?
                removeParameter1(name, value, queryString.toString(), pairs) :
                this;
    }

    private UrlQueryString removeParameter1(final UrlParameterName name,
                                            final String value,
                                            final String queryString,
                                            final List<UrlParameterKeyValuePair> pairs) {
        final Map<UrlParameterName, UrlParameterValueList> parameters = this.parametersCopy();
        if(null==value) {
            parameters.remove(name);
        } else {
            final UrlParameterValueList values = parameters.get(name);
            values.removeParameterValues(value);
            if(values.isEmpty()){
                parameters.remove(name);
            }
        }

        return queryString.isEmpty() ?
               EMPTY :
               new UrlQueryString(queryString, pairs, parameters);
    }

    /**
     * Makes a copy of all pairs.
     */
    private List<UrlParameterKeyValuePair> pairsCopy() {
        final List<UrlParameterKeyValuePair> copy = Lists.array();
        copy.addAll(this.pairs);
        return copy;
    }

    /**
     * Cache of {@link Map} of all parameters taken from the query string.
     */
    transient private List<UrlParameterKeyValuePair> pairs;

    // Object.........................................................................................................

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    /**
     * Compares the query string for equality.
     */
    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
               (other instanceof UrlQueryString &&
               this.equals0((UrlQueryString) other));
    }

    private boolean equals0(final UrlQueryString other) {
        return this.value().equals(other.value());
    }

    /**
     * Prints the original query string in its original form.
     */
    @Override
    public final String toString() {
        return this.queryString;
    }

    final void toString0(final StringBuilder b) {
        final String queryString = this.queryString;
        if(!queryString.isEmpty()){
            b.append(Url.QUERY_START.character());
            b.append(queryString);
        }
    }

    // Serializable....................................................................................................

    /**
     * Keeps {@link #EMPTY} a singleton
     */
    private Object readResolve() {
        final String value = this.queryString;
        return value.length() == 0 ? UrlQueryString.EMPTY : UrlQueryString.with(value);
    }

    private static final long serialVersionUID = -7394957571250582689L;
}
