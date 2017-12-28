package com.example.demo.config;

import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


//TODO http://www.mkyong.com/spring-security/spring-security-hibernate-annotation-example
// użyć zależnosci stad moze
//
// teraz były stad:http://blog.netgloo.com/2014/10/06/spring-boot-data-access-with-jpa-hibernate-and-mysql//
@EnableWebMvc
@Configuration
@ComponentScan({ "com.example.demo.*" })
@EnableTransactionManagement
@Import({ SpringSecurityConfig.class })
public class DatabaseConfig {


    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder =
                new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("com.example.demo.users.model")
                .addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();
        prop.put("hibernate.format_sql", "true");
        prop.put("hibernate.show_sql", "true");
        prop.put("hibernate.dialect",
                "org.hibernate.dialect.MySQL5Dialect");
        return prop;
    }


    @Bean(name = "dataSource")
    public BasicDataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setUsername("root");
        return ds;
    }

    //Create a transaction manager
    @Bean
    public HibernateTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }


    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver
                = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


//    /**
//     * Declare the JPA entity manager factory.
//     */
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean entityManagerFactory =
//                new LocalContainerEntityManagerFactoryBean();
//
//        entityManagerFactory.setDataSource(dataSource);
//
//        // Classpath scanning of @Component, @Service, etc annotated class
//        entityManagerFactory.setPackagesToScan("netgloo");
//
//        // Vendor adapter
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
//
//        // Hibernate properties
//        Properties additionalProperties = new Properties();
//        additionalProperties.put(
//                "hibernate.dialect",
//                "org.hibernate.dialect.MySQL5Dialect");
//        additionalProperties.put(
//                "hibernate.show_sql",
//                "true");
//        additionalProperties.put(
//                "hibernate.hbm2ddl.auto",
//                "update");
//        entityManagerFactory.setJpaProperties(additionalProperties);
//
//        return entityManagerFactory;
//    }
//
//    /**
//     * Declare the transaction manager.
//     */
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager =
//                new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                entityManagerFactory.getObject());
//        return transactionManager;
//    }
//
//    /**
//     * PersistenceExceptionTranslationPostProcessor is a bean post processor
//     * which adds an advisor to any bean annotated with Repository so that any
//     * platform-specific exceptions are caught and then rethrown as one
//     * Spring's unchecked data access exceptions (i.e. a subclass of
//     * DataAccessException).
//     */
//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//    // Private fields
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

}
