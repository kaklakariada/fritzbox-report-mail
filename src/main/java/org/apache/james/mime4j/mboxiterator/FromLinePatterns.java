/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2015 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.apache.james.mime4j.mboxiterator;

/**
 * Collection of From_ line patterns. Messages inside an mbox are separated by these lines. The pattern is usually
 * constant in a file but depends on the mail agents that wrote it. It's possible that more mailer agents wrote in the
 * same file using different From_ lines.
 */
public interface FromLinePatterns {

    /**
     * Match a line like: From ieugen@apache.org Fri Sep 09 14:04:52 2011
     */
    static final String DEFAULT = "^From \\S+@\\S.*\\d{4}$";
    /**
     * Matches other type of From_ line (without @): From MAILER-DAEMON Wed Oct 05 21:54:09 2011 Thunderbird mbox
     * content: From - Wed Apr 02 06:51:08 2014
     */
    static final String DEFAULT2 = "^From \\S+.*\\d{4}$";

}
