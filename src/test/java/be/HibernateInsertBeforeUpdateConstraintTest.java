package be;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "classpath:/hibernate-config-embedded.xml" })
public class HibernateInsertBeforeUpdateConstraintTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private SessionFactory sessionFactory;

	public void testWithoutFlushToPersistTheUpdate() {

		Example example = new Example();

		example.setVal("val1");

		sessionFactory.getCurrentSession().save(example);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		//Change 'val1' to another still unique value, 'val2' in this case
		example = sessionFactory.getCurrentSession().get(Example.class, example.getId());
		example.setVal("val2");

		//Save another Example, this time using 'gemeente1' as its value
		Example example2 = new Example();
		example2.setVal("val1");
		sessionFactory.getCurrentSession().save(example2);

		try {
			sessionFactory.getCurrentSession().flush();
			Assert.fail();
		} catch (Exception e) {
			//expected
			//hibernate first does the insert of example2 and than the update of example.
			//the insert of example2 will throw ConstraintViolation excepten because example is not updated yet. How come?
		}
	}

	public void testWithFlushToPersistTheUpdate() {
		Example example3 = new Example();
		example3.setVal("val3");

		sessionFactory.getCurrentSession().save(example3);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		//Change 'gemeente3' to another still unique value, 'gemeente4' in this case
		example3 = sessionFactory.getCurrentSession().get(Example.class, example3.getId());
		example3.setVal("val4");
		sessionFactory.getCurrentSession().saveOrUpdate(example3);

		//!!!!!!!!!!! EXPLICIT EXTRA CALL FLUSH TO AVOID CONSTRAINT VIOLATION !!!!!!!!!!!!!!!!
		sessionFactory.getCurrentSession().flush();

		//Save another Example, this time using 'gemeente3' as its value
		Example example4 = new Example();
		example4.setVal("val3");

		//No constraintviolation
		sessionFactory.getCurrentSession().save(example4);
		sessionFactory.getCurrentSession().flush();
	}
}
