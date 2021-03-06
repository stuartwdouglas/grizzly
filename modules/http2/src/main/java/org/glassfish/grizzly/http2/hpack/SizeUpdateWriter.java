/*
 * Copyright (c) 2016, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.grizzly.http2.hpack;

import org.glassfish.grizzly.Buffer;

final class SizeUpdateWriter implements BinaryRepresentationWriter {

    private final IntegerWriter intWriter = new IntegerWriter();
    private int maxSize;
    private boolean tableUpdated;

    SizeUpdateWriter() { }

    SizeUpdateWriter maxHeaderTableSize(int size) {
        intWriter.configure(size, 5, 0b0010_0000);
        this.maxSize = size;
        return this;
    }

    @Override
    public boolean write(HeaderTable table, Buffer destination) {
        if (!tableUpdated) {
            table.setMaxSize(maxSize);
            tableUpdated = true;
        }
        return intWriter.write(destination);
    }

    @Override
    public BinaryRepresentationWriter reset() {
        intWriter.reset();
        maxSize = -1;
        tableUpdated = false;
        return this;
    }
}
