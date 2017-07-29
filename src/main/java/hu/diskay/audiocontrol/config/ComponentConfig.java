package hu.diskay.audiocontrol.config;

import hu.diskay.audiocontrol.component.ApplicationEventListener;
import org.springframework.context.annotation.Import;

@Import(ApplicationEventListener.class)
public class ComponentConfig {

}
