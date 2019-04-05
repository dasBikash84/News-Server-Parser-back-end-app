package com.dasbikash.news_server_parser.bootstrap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class BootstrapSettings {
	public static void main(String[] args) {

		Configuration configuration = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml"));
		SessionFactory sessionFactory = configuration.buildSessionFactory();

		try (Session session = sessionFactory.openSession()) {

			if (NewsPaperSettingsBootStrapFromRealTimeDb.INSTANCE.saveDefaultSettings(session)){
				System.out.println("Settings loaded.");
			}else {
				System.out.println("Settings loading failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
