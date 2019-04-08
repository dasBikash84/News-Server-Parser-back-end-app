/*
 * Copyright 2019 das.bikash.dev@gmail.com. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasbikash.news_server_parser.bootstrap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class BootstrapSettings {
	public static void main(String[] args) {

		Configuration configuration = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml"));
		SessionFactory sessionFactory = configuration.buildSessionFactory();

		boolean loadSettings = false;

		try (Session session = sessionFactory.openSession()) {

			if (loadSettings) {
				if (NewsPaperSettingsBootStrapFromRealTimeDb.INSTANCE.saveDefaultSettings(session)) {
					System.out.println("Settings loaded.");
				} else {
					System.out.println("Settings loading failed.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
