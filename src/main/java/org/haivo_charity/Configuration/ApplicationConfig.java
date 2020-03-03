package org.haivo_charity.Configuration;

import org.haivo_charity.formatter.AccountFormatter;
import org.haivo_charity.formatter.CategoryFormatter;
import org.haivo_charity.formatter.VolunteerFormatter;
import org.haivo_charity.formatter.VoteFormatter;
import org.haivo_charity.service.*;
import org.haivo_charity.service.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan("org.haivo_charity")
@EnableJpaRepositories("org.haivo_charity.repository")
public class ApplicationConfig implements ApplicationContextAware, WebMvcConfigurer {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //Thymeleaf Configuration
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setContentType("text/html; charset=utf-8");
        return viewResolver;
    }

    //JPA configuration
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory){
        return entityManagerFactory.createEntityManager();
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(new String[]{"org.haivo_charity.model"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            URL res = getClass().getClassLoader().getResource("database.properties");
            assert res != null;
            Properties prop = new Properties();
            InputStream inputStream = new FileInputStream(Paths.get(res.toURI()).toFile());
            prop.load(inputStream);
            dataSource.setDriverClassName(prop.getProperty("driver"));
            String urlDatabase = prop.getProperty("type") + prop.getProperty("host") +
                    prop.getProperty("name") + prop.getProperty("config");
            dataSource.setUrl(urlDatabase);
            dataSource.setUsername(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            return dataSource;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    private Properties additionalProperties(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.connection.useUnicode","true");
        properties.setProperty("hibernate.connection.characterEncoding","UTF-8");
        properties.setProperty("hibernate.connection.charSet","UTF-8");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(7 * 1024 * 1024);
        return multipartResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addFormatter(new VolunteerFormatter(applicationContext.getBean(VolunteerService.class)));
        registry.addFormatter(new VoteFormatter(applicationContext.getBean(VoteService.class)));
        registry.addFormatter(new CategoryFormatter(applicationContext.getBean(CategoryService.class)));
        registry.addFormatter(new AccountFormatter(applicationContext.getBean(AccountService.class)));
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("/WEB-INF/fonts/").resourceChain(false);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/WEB-INF/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/WEB-INF/js/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("/WEB-INF/img/");
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }

    @Bean
    public DonateService donateService(){
        return new DonateServiceImpl();
    }
    @Bean
    public VoteService voteService(){
        return new VoteServiceImpl();
    }
    @Bean
    public VolunteerService volunteerService(){
        return new VolunteerServiceImpl();
    }
    @Bean
    public CategoryService categoryService(){
        return new CategoryServiceImpl();
    }
    @Bean
    public VoteImageService voteImageService(){
        return new VoteImageServiceImpl();
    }
    @Bean
    public AccountService accountService(){
        return new AccountServiceImpl();
    }
    @Bean
    public EventService eventService(){
        return new EventServiceImpl();
    }
    @Bean
    public RegisterEventService registerEventService(){
        return new RegisterEventServiceImpl();
    }

}
