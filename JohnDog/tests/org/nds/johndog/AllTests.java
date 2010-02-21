package org.nds.johndog;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.nds.johndog.web.service.ServiceTest;

@RunWith(Suite.class)
@SuiteClasses(value={
ServiceTest.class,
})
public class AllTests{
}
