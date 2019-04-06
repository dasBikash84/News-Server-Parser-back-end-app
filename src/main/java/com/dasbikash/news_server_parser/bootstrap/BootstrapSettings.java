package com.dasbikash.news_server_parser.bootstrap;

import com.dasbikash.news_server_parser.model.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.File;
import java.util.List;

public class BootstrapSettings {
	public static void main(String[] args) {

		Configuration configuration = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml"));
		SessionFactory sessionFactory = configuration.buildSessionFactory();

		try (Session session = sessionFactory.openSession()) {

			/*if (NewsPaperSettingsBootStrapFromRealTimeDb.INSTANCE.saveDefaultSettings(session)){
				System.out.println("Settings loaded.");
			}else {
				System.out.println("Settings loading failed.");
			}*/

            String hql = "FROM Country where name='Bangladesh'";
            Query query = session.createQuery(hql);
            List<Country> results = query.list();
            System.out.println(results.get(0).getCountryCode());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
