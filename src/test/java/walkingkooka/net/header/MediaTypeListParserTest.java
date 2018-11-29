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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class MediaTypeListParserTest extends MediaTypeParserTestCase<MediaTypeListParser> {

    @Test
    public void testTrailingComma() {
        this.parseAndCheckOne("type/subtype,", TYPE, SUBTYPE, MediaType.NO_PARAMETERS);
    }

    @Test
    public void testTrailingComma2() {
        this.parseAndCheckOne("type/subtype;parameter123=value456,", TYPE, SUBTYPE, parameters("parameter123", "value456"));
    }

    @Test
    public void testTypeSubTypeWhitespace4() {
        this.parseAndCheck2("type1/subtype1 ",
                MediaType.with("type1", "subtype1"));
    }

    @Test
    public void testTypeSubTypeCommaWhitespaceTypeSubType() {
        this.parseAndCheck2("type1/subtype1, type2/subtype2",
                MediaType.with("type1", "subtype1"),
                MediaType.with("type2", "subtype2"));
    }

    @Test
    public void testTypeSubTypeParameterCommaWhitespaceTypeSubType2() {
        this.parseAndCheck2("type1/subtype1;p1=v1, type2/subtype2",
                MediaType.with("type1", "subtype1").setParameters(this.parameters("p1", "v1")),
                MediaType.with("type2", "subtype2"));
    }

    @Test
    public void testTypeSubTypeParameterCommaTypeSubTypeParameter() {
        this.parseAndCheck2("type1/subtype1;p1=v1,type2/subtype2;p2=v2",
                MediaType.with("type1", "subtype1").setParameters(this.parameters("p1", "v1")),
                MediaType.with("type2", "subtype2").setParameters(this.parameters("p2", "v2")));
    }

    @Test
    public void testTypeSubTypeParameterCommaWhitespaceTypeSubTypeParameter() {
        this.parseAndCheck2("type1/subtype1;p1=v1, type2/subtype2;p2=v2",
                MediaType.with("type1", "subtype1").setParameters(this.parameters("p1", "v1")),
                MediaType.with("type2", "subtype2").setParameters(this.parameters("p2", "v2")));
    }

    @Test
    public void testTypeSubTypeWhitespaceParameterCommaWhitespaceTypeSubTypeWhitespaceParameter() {
        this.parseAndCheck2("type1/subtype1; p1=v1, type2/subtype2; p2=v2",
                MediaType.with("type1", "subtype1").setParameters(this.parameters("p1", "v1")),
                MediaType.with("type2", "subtype2").setParameters(this.parameters("p2", "v2")));
    }

    @Test
    public void testSortedByQFactor() {
        this.parseAndCheck2("type1/subtype1; q=0.25, type2/subtype2; q=1.0, type3/subtype3; q=0.5",
                MediaType.with("type2", "subtype2").setParameters(this.parameters("q", "1.0")),
                MediaType.with("type3", "subtype3").setParameters(this.parameters("q", "0.5")),
                MediaType.with("type1", "subtype1").setParameters(this.parameters("q", "0.25")));
    }

    @Override
    final void parseAndCheck(final String text,
                                       final String type,
                                       final String subtype,
                                       final Map<MediaTypeParameterName, String> parameters) {
        parseAndCheckOne(text, type, subtype, parameters);
        parseAndCheckRepeated(text, type, subtype, parameters);
        parseAndCheckSeveral(text, type, subtype, parameters);
    }

    private void parseAndCheckOne(final String text,
                                  final String type,
                                  final String subtype,
                                  final Map<MediaTypeParameterName, String> parameters) {
        final List<MediaType> result = MediaTypeListParser.parseList(text);
        assertEquals("parse " + CharSequences.quote(text) + " got " + result, 1, result.size());
        this.check(result.get(0), type, subtype, parameters);
    }

    private void parseAndCheckRepeated(final String text,
                                       final String type,
                                       final String subtype,
                                       final Map<MediaTypeParameterName, String> parameters) {
        final String parsed = text + MediaType.MEDIATYPE_SEPARATOR + text;
        final List<MediaType> result = MediaTypeListParser.parseList(parsed);
        assertEquals("parse " + CharSequences.quote(parsed) + " got " + result, 2, result.size());
        this.check(result.get(0), type, subtype, parameters);
        this.check(result.get(1), type, subtype, parameters);
    }

    private void parseAndCheckSeveral(final String text,
                                      final String type,
                                      final String subtype,
                                      final Map<MediaTypeParameterName, String> parameters) {
        final String parsed = "TYPE1/SUBTYPE1," + text + ",TYPE2/SUBTYPE2;x=y," + text;
        final List<MediaType> result = MediaTypeListParser.parseList(parsed);

        assertEquals("parse " + CharSequences.quote(parsed) + " got " + result, 4, result.size());

        this.check(result.get(0), "TYPE1", "SUBTYPE1");
        this.check(result.get(1), type, subtype, parameters);

        this.check(result.get(2), "TYPE2", "SUBTYPE2", parameters("x", "y"));
        this.check(result.get(3), type, subtype, parameters);
    }

    private void parseAndCheck2(final String text, final MediaType...mediaTypes) {
        assertEquals("Incorrect result parsing " + CharSequences.quote(text),
                Lists.of(mediaTypes),
                MediaTypeListParser.parseList(text));
    }

    @Override
    protected Class<MediaTypeListParser> type() {
        return MediaTypeListParser.class;
    }
}
