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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.NodeTestCase;
import walkingkooka.tree.select.NodeSelector;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public final class FileNodeTest extends NodeTestCase<FileNode, FileNodeName, FileNodeAttributeName, String> {

    private final static String SUB = "sub";
    private final static String SUB_SUB = "subSub";
    private final static String SUB_FILE = "subFile";
    private final static String SUB_FILE2 = "subFile2";

    private final static String CONTENT1 = "content-1";
    private final static String CONTENT2 = "content-2";
    private final static String DIFFERENT_CONTENT_TEXT = "DIFFERENT_CONTENT_TEXT-content-text";

    @Before
    public void createDirectoryStructure() throws IOException{
        home = Files.createTempDirectory(FileNodeTest.class.getName() + "-");

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

    @After
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
    static Map<Path, Map<FileNodeCacheAtom, String>> fileNodeToCache = Maps.ordered();

    // Tests ...........................................................................................................

    @Test
    public void testParentOfRoot() {
        this.checkWithoutParent(this.createNode());
    }

    @Test
    public void testDirectoryAttributesKeySet() {
        assertEquals(DirectoryFileNode.ATTRIBUTE_NAMES, this.createNode().attributes().keySet());
    }

    @Test
    public void testDirectoryAttributesValues() {
        this.checkAttributes(this.createNode(), DirectoryFileNode.ATTRIBUTE_NAMES);
    }

    @Test
    public void testChildrenOfRoot() {
        final FileNode root = this.createNode();
        final List<FileNode> children = root.children();
        assertEquals("child count=" + children, 1, children.size());

        assertSame("children cached", children, root.children());

        final FileNode child = root.children().get(0);
        assertEquals("child", FileNodeName.with("sub"), child.name());
    }

    @Test
    public void testCreatedLastAccessedModifiedAttributes() {
        final FileNode root = this.createNode();

        final String today = DateTimeFormatter.ISO_DATE.format(LocalDateTime.now());
        checkAttributeContains(root, FileNodeAttributeName.CREATED, today);
        checkAttributeContains(root, FileNodeAttributeName.LAST_ACCESSED, today);
        checkAttributeContains(root, FileNodeAttributeName.LAST_MODIFIED, today);
    }

    @Test
    public void testHidden() {
        final FileNode root = this.createNode();

        checkAttributeEquals(root, FileNodeAttributeName.HIDDEN, Boolean.FALSE.toString());
    }

    @Test
    public void testChildrenOfGraphAndAttributes() {
        final FileNode root = this.createNode();
        final List<FileNode> children = root.children();
        assertEquals("child count=" + children, 1, children.size());

        assertSame("children cached", children, root.children());

        final FileNode child = root.children().get(0);
        assertEquals("child", sub(), child.name());

        FileNode subSub = null;
        FileNode subFile = null;
        FileNode subFile2 = null;

        for(FileNode subChild : child.children()) {
            final FileNodeName name = subChild.name();
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

        checkAttributeEquals(subSub, FileNodeAttributeName.TYPE, FileNode.DIRECTORY_TYPE);
        checkAttributeEquals(subSub, FileNodeAttributeName.HIDDEN, Boolean.FALSE.toString());

        checkAttributeEquals(subFile, FileNodeAttributeName.TYPE, FileNode.FILE_TYPE);
        checkAttributeEquals(subFile2, FileNodeAttributeName.TYPE, FileNode.FILE_TYPE);
    }

    private void checkIsDirectoryAndIsFile(final FileNode node, final boolean isDirectory) {
        assertEquals(node + " isDirectory()", node.isDirectory(), isDirectory);
        assertEquals(node + " isFile()", node.isFile(), !isDirectory);
    }

    @Test
    public void testFile() {
        final FileNode subFile = this.subFileFileNode();
        this.checkAttributeEquals(subFile, FileNodeAttributeName.SIZE, "" + CONTENT1.length());
        this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);
        this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);
        this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);
    }

    @Test
    public void testFileTextRepeatedAfterClearingCache() {
        final FileNode subFile = this.subFileFileNode();
        final String text = this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);
        final String textAgain = this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);
        assertSame("text was not cached", text, textAgain);
    }

    @Test
    public void testFileCreatedDoesntChangeAfterWrite() throws Exception {
        final FileNode subFile = this.subFileFileNode();
        this.checkAttributeEquals(subFile, FileNodeAttributeName.TEXT, CONTENT1);

        final String created = attribute(subFile, FileNodeAttributeName.CREATED);
        writeFile(subFile.value(), DIFFERENT_CONTENT_TEXT);

        this.checkAttributeEquals(subFile, FileNodeAttributeName.CREATED, created);
    }

    @Test
    public void testFileAttributesKeySet() {
        assertEquals(FileFileNode.ATTRIBUTE_NAMES, this.subFileFileNode().attributes().keySet());
    }

    @Test
    public void testFileAttributesValues() {
        this.checkAttributes(this.subFileFileNode(),  FileFileNode.ATTRIBUTE_NAMES);
    }

    private void checkAttributes(final FileNode node, final Set<FileNodeAttributeName> names) {
        final Map<FileNodeAttributeName, String> read = node.attributes();
        assertEquals("read attributes size", names.size(), read.size());

        final Map<FileNodeAttributeName, String> attributes = Maps.ordered();
        for(Entry<FileNodeAttributeName, String> nameAndValue : read.entrySet()) {
            final FileNodeAttributeName name = nameAndValue.getKey();
            assertNotNull("name key must not be null", name);
            final String value = nameAndValue.getValue();
            assertNotNull("value must not be null", value);

            attributes.put(name, value);
        }
        attributes.putAll(read);

        assertEquals("attribute values collection", toList(attributes.values()), toList(read.values()));
    }

    private static List<String> toList(final Collection<String> values) {
        final List<String> list = Lists.array();
        list.addAll(values);
        return list;
    }

    @Test
    public void testSelectorUsage() throws Exception {
        final FileNode document = this.createNode();
        final NodeSelector<FileNode, FileNodeName, FileNodeAttributeName, String> selector = FileNode.absoluteNodeSelectorBuilder()
                .descendant()
                .named(FileNodeName.with(SUB_FILE))
                .build();
        final Set<FileNode> matches = selector.accept(document, selector.nulObserver());
        assertEquals("should have matched a single file\n" + matches, 1, matches.size());
    }

    private FileNode subFileFileNode() {
        final FileNode root = this.createNode();
        final FileNode sub = root.children().get(0);
        for(FileNode subChild : sub.children()) {
            final FileNodeName name = subChild.name();
            if(name.equals(subFile())){
                return subChild;
            }
        }
        fail("didnt find " + SUB_FILE);
        return null;
    }

    private FileNodeName sub() {
        return FileNodeName.with(SUB);
    }

    private FileNodeName subSub() {
        return FileNodeName.with(SUB_SUB);
    }

    private FileNodeName subFile() {
        return FileNodeName.with(SUB_FILE);
    }

    private FileNodeName subFile2() {
        return FileNodeName.with(SUB_FILE2);
    }

    private String attribute(final FileNode node, final FileNodeAttributeName attribute) {
        return node.attributes().get(attribute);
    }

    private String checkAttributeEquals(final FileNode node, final FileNodeAttributeName attribute, final String value) {
        final String actual = node.attributes().get(attribute);
        assertEquals(node.value().getFileName() + "." + attribute, value, actual);
        return actual;
    }

    private String checkAttributeContains(final FileNode node, final FileNodeAttributeName attribute, final String value) {
        final String actual = node.attributes().get(attribute);
        assertTrue(node.value().getFileName() + "." + attribute + "=" + CharSequences.quote(actual) + " doesnt contain " + CharSequences.quote(value), actual.contains(value));
        return actual;
    }

    // Helpers ...........................................................................................................

    @Override
    protected FileNode createNode() {
        return FileNode.directory(home, new FileNodeContext() {

            private Map<Path, FileNode> pathToFileNode = Maps.ordered();

            @Override
            public Path rootPath() {
                return home;
            }

            @Override
            public FileNode directory(final Path path) {
                FileNode node = pathToFileNode.get(path);
                if(null == node) {
                    node = FileNode.directory(path, this);
                    pathToFileNode.put(path, node);
                }
                return node;
            }

            @Override
            public FileNode file(final Path path) {
                FileNode node = pathToFileNode.get(path);
                if(null == node) {
                    node = FileNode.file(path, this);
                    pathToFileNode.put(path, node);
                }
                return node;
            }

            @Override
            public boolean mustLoad(final FileNode node, final FileNodeCacheAtom atom) {
                final Path path = node.value();
                Map<FileNodeCacheAtom, String> fileNodeCache = fileNodeToCache.get(path);
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
    protected String requiredNamePrefix() {
        return "File";
    }

    @Override
    protected Class<FileNode> type() {
        return FileNode.class;
    }
}
