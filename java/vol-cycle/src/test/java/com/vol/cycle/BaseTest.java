/**
 * 
 */
package com.vol.cycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author scott
 *
 */
@ContextConfiguration(locations = {"classpath:vol-dao-beans.xml","classpath:vol-cycle-beans.xml"})
public abstract class BaseTest extends AbstractJUnit4SpringContextTests{
	public final Logger log = LoggerFactory.getLogger(getClass());
}
