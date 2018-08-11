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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class UrlQueryStringTest extends HashCodeEqualsDefinedTestCase<UrlQueryString> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        UrlQueryString.with(null);
    }

    @Test
    public void testWithEmpty() {
        final UrlQueryString queryString = UrlQueryString.with("");
        assertSame(UrlQueryString.EMPTY, queryString);

        assertEquals("parameters", Maps.empty(), queryString.parameters());
        assertEquals("parameters.keySet", Sets.empty(), queryString.parameters().keySet());

        this.parameterAbsent(queryString, "abc");
    }

    @Test
    public void testKeyOnly() {
        final UrlQueryString queryString = UrlQueryString.with("a");

        this.parameterValuesAndCheck(queryString, "a", "");
    }

    @Test
    public void testKeyOnly2() {
        final UrlQueryString queryString = UrlQueryString.with("abc");

        this.parameterValuesAndCheck(queryString, "abc", "");
    }

    @Test
    public void testKeyValuePair() {
        final UrlQueryString queryString = UrlQueryString.with("a=z");

        this.parameterValuesAndCheck(queryString, "a", "z");
    }

    @Test
    public void testKeyValuePair2() {
        final UrlQueryString queryString = UrlQueryString.with("abc=def");

        this.parameterValuesAndCheck(queryString, "abc", "def");
    }

    @Test
    public void testTwoKeyValuePair() {
        final UrlQueryString queryString = UrlQueryString.with("a=b&c=d");

        this.parameterValuesAndCheck(queryString, "a", "b");
        this.parameterValuesAndCheck(queryString, "c", "d");
    }

    @Test
    public void testTwoKeyValuePair2() {
        final UrlQueryString queryString = UrlQueryString.with("abc=def&ghi=jkl");

        this.parameterValuesAndCheck(queryString, "abc", "def");
        this.parameterValuesAndCheck(queryString, "ghi", "jkl");
    }

    @Test
    public void testKeyValueSemiColonKeyValue2() {
        final UrlQueryString queryString = UrlQueryString.with("abc=def;ghi=jkl");

        this.parameterValuesAndCheck(queryString, "abc", "def");
        this.parameterValuesAndCheck(queryString, "ghi", "jkl");
    }

    @Test
    public void testKeyMultipleValues() {
        final UrlQueryString queryString = UrlQueryString.with("a=1&a=2");

        this.parameterValuesAndCheck(queryString, "a", "1", "2");
    }

    @Test
    public void testKeyMultipleValues2() {
        final UrlQueryString queryString = UrlQueryString.with("a=1&def=ghi&a=2&a=3");

        this.parameterValuesAndCheck(queryString, "a", "1", "2", "3");
        this.parameterValuesAndCheck(queryString, "def", "ghi");
    }

    // addParameter()

    @Test
    public void testAddParameterToEmpty() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        final UrlQueryString updated = empty.addParameter(this.name("a"), "1"); // verify encoded...

        this.parameterWithValueCheck(updated, "a", "1");

        this.checkToString(updated, "a=1");
    }

    @Test
    public void testAddParameterToEmptyEscaped() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        final UrlQueryString updated = empty.addParameter(this.name("a b"), "x y"); // verify encoded...

        this.parameterWithValueCheck(updated, "a b", "x y");

        this.checkToString(updated, "a+b=x+y"); // escaped form.
    }

    @Test
    public void testAddParameterTwiceToEmpty() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        final UrlQueryString updated = empty.addParameter(this.name("a"), "1")
                .addParameter(this.name("b"), "2");

        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");

        this.checkToString(updated, "a=1&b=2");
    }

    @Test
    public void testAddParameterThriceToEmpty() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        final UrlQueryString updated = empty.addParameter(this.name("a"), "1")
                .addParameter(this.name("b"), "2")
                .addParameter(this.name("c"), "3");

        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&b=2&c=3");
    }

    @Test
    public void testAddParameterThriceToEmpty2() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        final UrlQueryString updated = empty.addParameter(this.name("a"), "1")
                .addParameter(this.name("b"), "2")
                .addParameter(this.name("a"), "3");

        this.parameterWithValueCheck(updated, "a", "1", "3");
        this.parameterWithValueCheck(updated, "b", "2");

        this.checkToString(updated, "a=1&b=2&a=3");
    }

    @Test
    public void testAddParameterNonEmpty() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2");
        final UrlQueryString updated = first.addParameter(this.name("c"), "3");

        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&b=2&c=3");
    }

    @Test
    public void testAddParameterNonEmpty2() {
        final UrlQueryString first = UrlQueryString.with("a=1;b=2");
        final UrlQueryString updated = first.addParameter(this.name("c"), "3");

        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1;b=2;c=3");
    }

    // removeParameter(name) ..........................................................................................

    @Test
    public void testRemoveParameterNameEmpty() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        assertSame(empty, empty.removeParameter(name("absent")));
    }

    @Test
    public void testRemoveParameterNameAbsent() {
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        assertSame(queryString, queryString.removeParameter(name("absent")));
    }

    @Test
    public void testRemoveParameterNameOnly() {
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        assertSame(UrlQueryString.EMPTY, queryString.removeParameter(name("a")));
    }

    @Test
    public void testRemoveParameterNameOnlyMultipleValues() {
        final UrlQueryString queryString = UrlQueryString.with("a=b&a=c");
        assertSame(UrlQueryString.EMPTY, queryString.removeParameter(name("a")));
    }

    @Test
    public void testRemoveParameterNameNotOnly() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("a"));

        this.parameterAbsent(updated, "a");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2&c=3");
    }

    @Test
    public void testRemoveParameterNameNotOnly2() {
        final UrlQueryString first = UrlQueryString.with("a=1;b=2;c=3");
        final UrlQueryString updated = first.removeParameter(this.name("a"));

        this.parameterAbsent(updated, "a");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2;c=3");
    }

    @Test
    public void testRemoveParameterNameNotOnly3() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("b"));

        this.parameterAbsent(updated, "b");
        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&c=3");
    }

    @Test
    public void testRemoveParameterNameNotOnly4() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("c"));

        this.parameterAbsent(updated, "c");
        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");

        this.checkToString(updated, "a=1&b=2");
    }

    // removeParameter(name) ..........................................................................................

    @Test
    public void testRemoveParameterNameValueEmpty() {
        final UrlQueryString empty = UrlQueryString.EMPTY;
        assertSame(empty, empty.removeParameter(name("absent"), "1"));
    }

    @Test
    public void testRemoveParameterNameValueAbsent() {
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        assertSame(queryString, queryString.removeParameter(name("absent"), "1"));
    }

    @Test
    public void testRemoveParameterNameValueAbsentDifferentValue() {
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        assertSame(queryString, queryString.removeParameter(name("a"), "different"));
    }

    @Test
    public void testRemoveParameterNameValueOnly() {
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        assertSame(UrlQueryString.EMPTY, queryString.removeParameter(name("a"), "b"));
    }

    @Test
    public void testRemoveParameterNameValueOnlyMultipleValues() {
        final UrlQueryString queryString = UrlQueryString.with("a=b&a=b");
        assertSame(UrlQueryString.EMPTY, queryString.removeParameter(name("a"), "b"));
    }

    @Test
    public void testRemoveParameterNameValueNotOnly() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "1");
        assertNotSame(first, updated);

        this.parameterAbsent(updated, "a");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2&c=3");
    }

    @Test
    public void testRemoveParameterNameValueNotOnly2() {
        final UrlQueryString first = UrlQueryString.with("a=1;b=2;c=3");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "1");
        assertNotSame(first, updated);

        this.parameterAbsent(updated, "a");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2;c=3");
    }

    @Test
    public void testRemoveParameterNameValueNotOnly3() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("b"), "2");
        assertNotSame(first, updated);

        this.parameterAbsent(updated, "b");
        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&c=3");
    }

    @Test
    public void testRemoveParameterNameValueNotOnly4() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3");
        final UrlQueryString updated = first.removeParameter(this.name("c"), "3");
        assertNotSame(first, updated);

        this.parameterAbsent(updated, "c");
        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");

        this.checkToString(updated, "a=1&b=2");
    }

    @Test
    public void testRemoveParameterNameValueNotOnlyIgnoresDifferentValue() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3&a=4");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "4");
        assertNotSame(first, updated);

        this.parameterWithValueCheck(updated, "a", "1");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&b=2&c=3");
    }

    @Test
    public void testRemoveParameterNameValueNotOnlyIgnoresDifferentValue2() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3&a=4");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "1");
        assertNotSame(first, updated);

        this.parameterWithValueCheck(updated, "a", "4");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2&c=3&a=4");
    }

    @Test
    public void testRemoveParameterNameValueNotOnlyIgnoresDifferentValue3() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3&a=4&a=1");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "1");
        assertNotSame(first, updated);

        this.parameterWithValueCheck(updated, "a", "4");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "b=2&c=3&a=4");
    }

    @Test
    public void testRemoveParameterNameValueNotOnlyIgnoresDifferentValue4() {
        final UrlQueryString first = UrlQueryString.with("a=1&b=2&c=3&a=4&a=1");
        final UrlQueryString updated = first.removeParameter(this.name("a"), "4");
        assertNotSame(first, updated);

        this.parameterWithValueCheck(updated, "a", "1", "1");
        this.parameterWithValueCheck(updated, "b", "2");
        this.parameterWithValueCheck(updated, "c", "3");

        this.checkToString(updated, "a=1&b=2&c=3&a=1");
    }

    // helpers ............................................................................................................

    private void parameterWithValueCheck(final UrlQueryString queryString, final String name, final String...values) {
        this.parameterAndCheck(queryString, name, values[0]);
        this.parameterValuesAndCheck(queryString, name, values);
        this.parametersGetAndCheck(queryString, name, values);
    }

    private void parameterAbsent(final UrlQueryString queryString, final String key) {
        this.parameterAndCheck0(queryString, key, Optional.empty());
        this.parameterValuesAndCheck(queryString, key);
        this.parametersGetAndCheck0(queryString, key, null);
    }

    // UrlQueryString.parameter(String) -> Optional.

    private void parameterAndCheck(final UrlQueryString queryString, final String key, final String value){
        parameterAndCheck0(queryString, key, Optional.of(value));
    }

    private void parameterAndCheck0(final UrlQueryString queryString, final String key, final Optional<String> value){
        assertEquals("UrlQueryString.parameter(" + CharSequences.quote(key) + ") in: " + queryString, value, queryString.parameter(this.name(key)));
    }

    // UrlQueryString.parameterValues(String) -> List<String> never null.

    private void parameterValuesAndCheck(final UrlQueryString queryString, final String key, final String...values){
        parameterValuesAndCheck0(queryString, key, Lists.of(values));
    }

    private void parameterValuesAndCheck0(final UrlQueryString queryString, final String key, final List<String> values){
        assertEquals("UrlQueryString.parameterValues(" + CharSequences.quote(key) + ") in: " + queryString, values, queryString.parameterValues(this.name(key)));
    }

    // UrlQueryString.parameters().get() -> List

    private void parametersGetAbsent(final UrlQueryString queryString, final String key) {
        parametersGetAndCheck0(queryString, key, Lists.empty());
    }

    private void parametersGetAndCheck(final UrlQueryString queryString, final String key, final String...values) {
        parametersGetAndCheck0(queryString, key, Lists.of(values));
    }

    private void parametersGetAndCheck0(final UrlQueryString queryString, final String key, final List<String> values) {
        assertEquals("UrlQueryString.parameters().get(" + CharSequences.quote(key) + ") in: " + queryString, values, queryString.parameters().get(this.name(key)));
    }

    private UrlParameterName name(final String name) {
        return UrlParameterName.with(name);
    }

    private void checkToString(final UrlQueryString queryString, final String toString) {
        assertEquals("UrlQueryString.toString failure", toString, queryString.toString());
    }

    @Override
    protected Class<UrlQueryString> type() {
        return UrlQueryString.class;
    }
}
