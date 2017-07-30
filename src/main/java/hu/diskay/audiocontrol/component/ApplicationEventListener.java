package hu.diskay.audiocontrol.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

public class ApplicationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventListener.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        LOG.info("Context refreshed: {}, {}", applicationContext.getId(), contextRefreshedEvent.getSource());
    }

    @EventListener
    public void HandleContextStart(ContextStartedEvent contextStartedEvent) {
        ApplicationContext applicationContext = contextStartedEvent.getApplicationContext();
        LOG.info("Context started: {}", applicationContext.getId());
    }
}
