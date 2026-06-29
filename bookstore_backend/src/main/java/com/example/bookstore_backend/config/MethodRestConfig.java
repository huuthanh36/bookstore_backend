package com.example.bookstore_backend.config;

import com.example.bookstore_backend.entity.TheLoai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MethodRestConfig implements RepositoryRestConfigurer {

    private String url ="http://localhost:5173";

    @Autowired
    private EntityManager entityManager;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        //expose ids
        //Cho phép trả bề id
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));

        //CORS configuration
        cors.addMapping("/**").allowedOrigins(url).allowedMethods("GET", "POST", "PUT", "DELETE");
        // expose ids
        // Cho phép id trong khi trả về json
//        Class[] classes = entityManager.getMetamodel()
//                .getEntities().stream().map(Type::getJavaType).toArray(Class[]::new);
//        config.exposeIdsFor(classes);

        // Chỉ có thể loại hiển thị id
        //config.exposeIdsFor(TheLoai.class);

        // Chặn các method
        HttpMethod[] chanCacPhuongThuc ={
                HttpMethod.GET,
                HttpMethod.PUT,
                HttpMethod.PATCH,
                HttpMethod.DELETE
        };

        disableHttpMethods(TheLoai.class, config, chanCacPhuongThuc);

    }

    private void disableHttpMethods(Class c, RepositoryRestConfiguration config, HttpMethod[] methods){
        config.getExposureConfiguration().forDomainType(c)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(methods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(methods));
    }
}
