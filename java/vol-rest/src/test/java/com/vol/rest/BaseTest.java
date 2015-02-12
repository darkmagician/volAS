/**
 * 
 */
package com.vol.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author scott
 *
 */
@ContextConfiguration(locations = {"classpath:vol-rest-beans.xml","classpath:vol-dao-beans.xml","classpath:vol-dao-test-beans.xml"})
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests{
	public final Logger log = LoggerFactory.getLogger(getClass());
}
