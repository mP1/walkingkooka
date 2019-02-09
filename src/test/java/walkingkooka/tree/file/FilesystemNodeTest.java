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

package walkingkooka.tree.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.NodeTesting;
import walkingkooka.type.MemberVisibility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public final class FilesystemNodeTest implements ClassTesting2<FilesystemNode>,
        NodeTesting<FilesystemNode, FilesystemNodeName, FilesystemNodeAttributeName, String> {

    private final static String SUB = "sub";
    private final static String SUB_SUB = "subSub";
    private final static String SUB_FILE = "subFile";
    private final static String SUB_FILE2 = "subFile2";

    private final static String CONTENT1 = "content-1";
    private final static String CONTENT2 = "content-2";
    private final static String DIFFERENT_CONTENT_TEXT = "DIFFERENT_CONTENT_TEXT-content-text";

    @BeforeEach
    public void createDirectoryStructure() throws IOException {
        home = Files.createTempDirectory(FilesystemNodeTest.class.getName() + "-");

        sub = Files.createDirectory(home.resolve(SUB)); // /sub
        subSub = Files.createDirectory(sub.resolve(SUB_SUB)); // /sub/subSub

        subFile = writeFile(sub.resolve(SUB_FILE), CONTENT1);
        subFile2 = writeFile(sub.resolve(SUB_FILE2), CONTENT2);

        fileNodeToCache.clear();
    }

    private static Path writeFile(final Path file, final String text) throws IOException {
        Files.write(file, text.getBytes(Charset.defaultCharset()));
        return file;
    }

    @AfterEach
    public void deleteDirectoryStructure() throws IOException{
        Files.delete(subFile);
        Files.delete(subFile2);
        Files.delete(subSub);
        Files.delete(sub);
        Files.delete(home);
    }

    private static Path home;
    private static Path sub;
    private static Path subFile;
    private static Path subFile2;
    private static Path subSub;

    /**
     * Static so tests can clear the cache by manipulating the map when they wish.
     */
    static Map<Path, Map<FilesystemNodeCacheAtom, String>> fileNodeToCache = Maps.ordered();

    // Tests ...........................................................................................................

    @Test
    public void testParentOfRoot() {
        this.checkWithoutParent(this.createNode());
    }

    @Test
    public void testDirectoryAttributesKeySet() {
        assertEquals(DirectoryFilesystemNode.ATTRIBUTE_NAMES, this.createNode().attributes().keySet());
    }

    @Test
    public void testDirectoryAttributesValues() {
        this.checkAttributes(this.createNode(), DirectoryFilesystemNode.ATTRIBUTE_NAMES);
    }

    @Test
    public void testChildrenOfRoot() {
        final FilesystemNode root = this.createNode();
        final List<FilesystemNode> children = root.children();
        assertEquals(1, children.size(), "child count=" + children);

        assertSame(children, root.children(), "children cached");

        final FilesystemNode child = root.children().get(0);
        assertEquals(FilesystemNodeName.with("sub"), child.name(), "child");
    }

    @Test
    public void testCreatedLastAccessedModifiedAttributes() {
        final FilesystemNode root = this.createNode();

        final String today = DateTimeFormatter.ISO_DATE.format(LocalDateTime.now());
        checkAttributeContains(root, FilesystemNodeAttributeName.CREATED, today);
        checkAttributeContains(root, FilesystemNodeAttributeName.LAST_ACCESSED, today);
        checkAttributeContains(root, FilesystemNodeAttributeName.LAST_MODIFIED, today);
    }

    @Test
    public void testHidden() {
        final FilesystemNode root = this.createNode();

        checkAttributeEquals(root, FilesystemNodeAttributeName.HIDDEN, Boolean.FALSE.toString());
    }

    @Test
    public void testChildrenOfGraphAndAttributes() {
        final FilesystemNode root = this.createNode();
        final List<FilesystemNode> children = root.children();
        assertEquals(1, children.size(), "child count=" + children);

        assertSame(children, root.children(),"children cached");

        final FilesystemNode child = root.children().get(0);
        assertEquals(sub(), child.name(), "child");

        FilesystemNode subSub = null;
        FilesystemNode subFile = null;
        FilesystemNode subFile2 = null;

        for(FilesystemNode subChild : child.children()) {
            final FilesystemNodeName name = subChild.name();
            if(name.equals(subSub())){
                subSub = subChild;
                continue;
            }
            if(name.equals(subFile())){
                subFile = subChild;
                continue;
            }
            if(name.equals(subFile2())){
                subFile2 = subChild;
                continue;
            }
        }

        assertNotNull(subSub);
        assertNotNull(subFile);
        assertNotNull(subFile2);

        checkIsDirectoryAndIsFile(subSub, true);
        checkIsDirectoryAndIsFile(subFile, false);
        checkIsDirectoryAndIsFile(subFile2, false);

        checkAttributeEquals(subSub, FilesystemNodeAttributeName.TYPE, FilesystemNode.DIRECTORY_TYPE);
        checkAttributeEquals(subSub, FilesystemNodeAttributeName.HIDDEN, Boolean.FALSE.toString());

        checkAttributeEquals(subFile, FilesystemNodeAttributeName.TYPE, FilesystemNode.FILE_TYPE);
        checkAttributeEquals(subFile2, FilesystemNodeAttributeName.TYPE, FilesystemNode.FILE_TYPE);
    }

    private void checkIsDirectoryAndIsFile(final FilesystemNode node, final boolean isDirectory) {
        assertEquals(node.isDirectory(), isDirectory, node + " isDirectory()");
        assertEquals(node.isFile(), !isDirectory,node + " isFile()");
    }

    @Test
    public void testFile() {
        final FilesystemNode subFile = this.subFileFileNode();
        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.SIZE, "" + CONTENT1.length());
        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);
        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);
        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);
    }

    @Test
    public void testFileTextRepeatedAfterClearingCache() {
        final FilesystemNode subFile = this.subFileFileNode();
        final String text = this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);
        final String textAgain = this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);
        assertSame(text, textAgain, "text was not cached");
    }

    @Test
    public void testFileCreatedDoesntChangeAfterWrite() throws Exception {
        final FilesystemNode subFile = this.subFileFileNode();
        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.TEXT, CONTENT1);

        final String created = attribute(subFile, FilesystemNodeAttributeName.CREATED);
        writeFile(subFile.value(), DIFFERENT_CONTENT_TEXT);

        this.checkAttributeEquals(subFile, FilesystemNodeAttributeName.CREATED, created);
    }

    @Test
    public void testFileAttributesKeySet() {
        assertEquals(FileFilesystemNode.ATTRIBUTE_NAMES, this.subFileFileNode().attributes().keySet());
    }

    @Test
    public void testFileAttributesValues() {
        this.checkAttributes(this.subFileFileNode(),  FileFilesystemNode.ATTRIBUTE_NAMES);
    }

    private void checkAttributes(final FilesystemNode node, final Set<FilesystemNodeAttributeName> names) {
        final Map<FilesystemNodeAttributeName, String> read = node.attributes();
        assertEquals(names.size(), read.size(), "read attributes size");

        final Map<FilesystemNodeAttributeName, String> attributes = Maps.ordered();
        for(Entry<FilesystemNodeAttributeName, String> nameAndValue : read.entrySet()) {
            final FilesystemNodeAttributeName name = nameAndValue.getKey();
            assertNotNull(name,"name key must not be null");
            final String value = nameAndValue.getValue();
            assertNotNull("value must not be null", value);

            attributes.put(name, value);
        }
        attributes.putAll(read);

        assertEquals(toList(attributes.values()), toList(read.values()),"attribute values collection");
    }

    private static List<String> toList(final Collection<String> values) {
        final List<String> list = Lists.array();
        list.addAll(values);
        return list;
    }

    @Test
    public void testSelectorUsage() {
        final FilesystemNode node = this.createNode();
        this.selectorAcceptAndCheckCount(node,
                FilesystemNode.PATH_SEPARATOR.absoluteNodeSelectorBuilder(FilesystemNode.class)
                        .descendant()
                        .named(FilesystemNodeName.with(SUB_FILE))
                        .build(),
                1);
    }

    private FilesystemNode subFileFileNode() {
        final FilesystemNode root = this.createNode();
        final FilesystemNode sub = root.children().get(0);
        for(FilesystemNode subChild : sub.children()) {
            final FilesystemNodeName name = subChild.name();
            if(name.equals(subFile())){
                return subChild;
            }
        }
        fail("didnt find " + SUB_FILE);
        return null;
    }

    private FilesystemNodeName sub() {
        return FilesystemNodeName.with(SUB);
    }

    private FilesystemNodeName subSub() {
        return FilesystemNodeName.with(SUB_SUB);
    }

    private FilesystemNodeName subFile() {
        return FilesystemNodeName.with(SUB_FILE);
    }

    private FilesystemNodeName subFile2() {
        return FilesystemNodeName.with(SUB_FILE2);
    }

    private String attribute(final FilesystemNode node, final FilesystemNodeAttributeName attribute) {
        return node.attributes().get(attribute);
    }

    private String checkAttributeEquals(final FilesystemNode node, final FilesystemNodeAttributeName attribute, final String value) {
        final String actual = node.attributes().get(attribute);
        assertEquals(value, actual, () -> node.value().getFileName() + "." + attribute);
        return actual;
    }

    private String checkAttributeContains(final FilesystemNode node, final FilesystemNodeAttributeName attribute, final String value) {
        final String actual = node.attributes().get(attribute);
        assertTrue(actual.contains(value),
                ()->node.value().getFileName() + "." + attribute + "=" + CharSequences.quote(actual) + " doesnt contain " + CharSequences.quote(value));
        return actual;
    }

    @Override
    public void testSetSameAttributes() {
        throw new UnsupportedOperationException();
    }

    // Helpers ...........................................................................................................

    @Override
    public FilesystemNode createNode() {
        return FilesystemNode.directory(home, new FilesystemNodeContext() {

            private Map<Path, FilesystemNode> pathToFileNode = Maps.ordered();

            @Override
            public Path rootPath() {
                return home;
            }

            @Override
            public FilesystemNode directory(final Path path) {
                FilesystemNode node = pathToFileNode.get(path);
                if(null == node) {
                    node = FilesystemNode.directory(path, this);
                    pathToFileNode.put(path, node);
                }
                return node;
            }

            @Override
            public FilesystemNode file(final Path path) {
                FilesystemNode node = pathToFileNode.get(path);
                if(null == node) {
                    node = FilesystemNode.file(path, this);
                    pathToFileNode.put(path, node);
                }
                return node;
            }

            @Override
            public boolean mustLoad(final FilesystemNode node, final FilesystemNodeCacheAtom atom) {
                final Path path = node.value();
                Map<FilesystemNodeCacheAtom, String> fileNodeCache = fileNodeToCache.get(path);
                if(null == fileNodeCache) {
                    fileNodeCache = Maps.hash();
                    fileNodeToCache.put(path, fileNodeCache);
                }

                return fileNodeCache.containsKey(atom);
            }

            // just read the text for the path.
            @Override
            public String text(final Path path) throws IOException{
                final char[] buffer = new char[4096];
                final StringBuilder text = new StringBuilder();
                try(final Reader reader = new InputStreamReader(new FileInputStream(path.toFile()))){
                    for(;;) {
                        final int read = reader.read(buffer);
                        if (-1 == read) {
                            break;
                        }
                        text.append(buffer, 0, read);
                    }
                }
                return text.toString();
            }
        });
    }

    @Override
    public String typeNamePrefix() {
        return "File";
    }

    @Override
    public Class<FilesystemNode> type() {
        return FilesystemNode.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
