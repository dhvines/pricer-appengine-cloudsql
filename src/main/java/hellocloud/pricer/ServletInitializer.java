
package hellocloud.pricer;

/**
 *  You must use WAR packaging to deploy into Google App Engine Standard.
 */

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer 
{
	  @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	  {
		  return application.sources(DefaultPricerApplication.class);
	  }	
}
