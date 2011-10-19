package org.hibernate.envers.test.integration.accesstype;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.AbstractEntityTest;
import org.hibernate.envers.test.Priority;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ImmutableClassAccessType extends AbstractEntityTest {
	private Country country;

	public void configure(Ejb3Configuration cfg) {
		cfg.addAnnotatedClass(Country.class);
	}

	@Test
    @Priority(10)
	public void initData() {
		EntityManager em = getEntityManager();

		// Revision 1
		em.getTransaction().begin();
		country = Country.of(123, "Germany");
		em.persist(country);
		em.getTransaction().commit();

	}

	@Test
	public void testRevisionsCounts() {
		assert Arrays.asList(1)
				.equals(
						getAuditReader().getRevisions(Country.class,
								country.getCode()));
	}

	@Test
	public void testHistoryOfId1() {
		Country country1 = getEntityManager().find(Country.class,
				country.getCode());
		assertEquals(country1, country);

		Country history = getAuditReader().find(Country.class, country1.getCode(), 1);
		assertEquals(country, history);
	}

}