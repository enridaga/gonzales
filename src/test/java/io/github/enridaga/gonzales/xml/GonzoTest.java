/*
 * Copyright (c) 2020 Enrico Daga @ http://www.enridaga.net
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.enridaga.gonzales.xml;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GonzoTest {
	private static final Logger log = LoggerFactory.getLogger(GonzoTest.class);

	/**
	 * File taken from
	 * https://msdn.microsoft.com/en-us/library/ms762271%28v=vs.85%29.aspx
	 */
	@Test
	public void books() throws GonzoException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("./books.xml");
		Gonza g = new Gonzo(is);
		String t = null;
		String p = null;
		while (g.stroll()) {
			// Meeting
			if (g.meets("/:catalog/:book/:title")) {
				t = g.pick();
			} else if (g.meets("/:catalog/:book/:price")) {
				p = g.pick();
			}

			// Leaving
			if (g.leaves("/:catalog/:book")) {
				// Print all
				log.info("{}: {}", t, p);
				// Resetting ...
				t = null;
				p = null;
			}

		}
	}
}
