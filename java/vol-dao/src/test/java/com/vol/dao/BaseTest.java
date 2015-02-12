/**
 * 
 */
package com.vol.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author scott
 *
 */
@ContextConfiguration(locations = {"classpath:vol-dao-beans.xml","classpath:vol-dao-test-beans.xml"})
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests{

}
