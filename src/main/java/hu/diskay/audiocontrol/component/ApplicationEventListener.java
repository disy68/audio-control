package hu.diskay.audiocontrol.component;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventListener.class);

    @Autowired
    private ApplicationContext context;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent contextRefreshedEvent) {
        DataSource dataSource = context.getBean(javax.sql.DataSource.class);
        LOG.info("DATASOURCE = " + dataSource);
    }
}
