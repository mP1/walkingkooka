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

import walkingkooka.Throwables;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Objects;

/**
 * Value class that holds a valid ip port. <br>
 * The constants are taken from <a href="http://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers></a>}.
 */
public final class IpPort implements Comparable<IpPort>, HashCodeEqualsDefined, Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * Tests if the given {@link int number} is a valid port.
     */
    public static boolean isPort(final int port) {
        return (port >= 0) && (port < 65536);
    }

    /**
     * A read only cache of already prepared {@link IpPort ports}.
     */
    final static Map<Integer, IpPort> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link IpPort} to the cache being built.
     */
    static IpPort registerConstant(final int port) {
        final IpPort ipPort = new IpPort(port);
        IpPort.CONSTANTS.put(port, ipPort);
        return ipPort;
    }

    /**
     * Ftp data port
     */
    public final static IpPort FTP_DATA = IpPort.registerConstant(20);

    /**
     * Ftp control port
     */
    public final static IpPort FTP_CONTROL = IpPort.registerConstant(21);

    /**
     * Secure Shell (SSH)
     */
    public final static IpPort SSH = IpPort.registerConstant(22);

    /**
     * Telnet port
     */
    public final static IpPort TELNET = IpPort.registerConstant(23);

    /**
     * Simple Mail Transfer Protocol
     */
    public final static IpPort SMTP = IpPort.registerConstant(25);

    /**
     * WHOIS port
     */
    public final static IpPort WHOIS = IpPort.registerConstant(43);

    /**
     * Domain Name System (DNS)
     */
    public final static IpPort DNS = IpPort.registerConstant(53);

    /**
     * FINGER port
     */
    public final static IpPort FINGER = IpPort.registerConstant(79);

    /**
     * Singleton holding the default web port 80.
     */
    public final static IpPort HTTP = IpPort.registerConstant(80);

    /**
     * Post Office Protocol v3 (POP3)
     */
    public final static IpPort POP3 = IpPort.registerConstant(110);

    /**
     * Simple file transfer protocol (SFTP)
     */
    public final static IpPort SFTP = IpPort.registerConstant(115);

    /**
     * Network News Transfer Protocol (NNTP)
     */
    public final static IpPort NNTP = IpPort.registerConstant(119);

    /**
     * Internet relay chat (IRC)
     */
    public final static IpPort IRC = IpPort.registerConstant(194);

    /**
     * Internet Message Access Protocol (IMAP)(
     */
    public final static IpPort IMAP = IpPort.registerConstant(220);

    /**
     * Lightweight Directory Access Protocol (LDAP)
     */
    public final static IpPort LDAP = IpPort.registerConstant(389);

    /**
     * HTTPS (Hypertext Transfer Protocol over SSL/TLS).
     */
    public final static IpPort HTTPS = IpPort.registerConstant(443);

    /**
     * Factory that creates a {@link IpPort}.
     */
    public static IpPort with(final int port) {
        if (false == IpPort.isPort(port)) {
            throw new IllegalArgumentException("Invalid port=" + port);
        }
        IpPort ipPort = IpPort.CONSTANTS.get(port);
        if (null == ipPort) {
            ipPort = new IpPort(port);
        }

        return ipPort;
    }

    /**
     * Returns a {@link IpPort} which was free by creating a {@link ServerSocket} and then releasing it.
     */
    public static IpPort free() {
        IpPort port = null;
        ServerSocket server = null;
        try {
            server = new ServerSocket(0);
            port = IpPort.with(server.getLocalPort());
        } catch (final Exception cause) {
            throw new IllegalStateException(Throwables.message("Unable to discover free port", cause), cause);
        } finally {
            if (null != server) {
                try {
                    server.close();
                } catch (final IOException ignore) {
                }
            }
        }
        return port;
    }

    /**
     * Private constructor use static factory
     */
    private IpPort(final int value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the port value as an integer
     */
    public int value() {
        return this.value;
    }

    private final int value;

    // Comparable

    @Override
    public int compareTo(final IpPort ipPort) {
        Objects.requireNonNull(ipPort, "ipPort");

        return this.value - ipPort.value;
    }

    // Serializable

    /**
     * Attempts to resolve to a constant singleton otherwise defaults to this.
     */
    public Object readResolve() {
        final IpPort port = IpPort.CONSTANTS.get(this.value);
        return null != port ? port : this;
    }

    // Object

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof IpPort) && this.equals((IpPort) other));
    }

    private boolean equals(final IpPort other) {
        return this.value == other.value;
    }

    /**
     * Dumps the port value.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    final void toString0(final StringBuilder b) {
        b.append(Url.HOST_PORT_SEPARATOR.character());
        b.append(this.value);
    }
}
