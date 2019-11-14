package hu.gamesgeek.restful;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServiceHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ServiceHelper.context = context;
    }

    public static <T extends Object> T getService(Class<T> serviceClazz) {
        return context.getBean(serviceClazz);
    }

}