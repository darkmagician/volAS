/**
 * 
 */
package com.vol.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author scott
 *
 */
@ContextConfiguration(locations = {"classpath:vol-dao-beans.xml","classpath:vol-dao-test-beans.xml"})
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests{

} 
