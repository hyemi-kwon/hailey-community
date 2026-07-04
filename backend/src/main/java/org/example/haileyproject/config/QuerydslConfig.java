package org.example.haileyproject.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//QueryDSL 사용을 위한 Spring Bean 설정 클래스
@Configuration//1. Spring IoC 컨테이너에게 이 클래스가 설정(Configuration) 클래스임을 명시하고, 내부의 @Bean 메서드들을 스캔하여 빈으로 등록하도록 지시.
public class QuerydslConfig {

    @PersistenceContext//@PersistenceContext:Spring이 관리하는 영속성 컨텍스트(Persistence Context)에 연결된 EntityManager를 의존성 주입(DI)
    private EntityManager entityManager;//EntityManager: 엔티티의 생명주기를 관리하고 데이터베이스에 대한 CRUD 및 쿼리 실행을 담당 안터페이스

    @Bean // 3. @Bean: 메서드의 반환 객체를 Spring Context가 관리하는 싱글톤 빈(Bean)으로 등록
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }//주입받은 EntityManager를 파라미터로 전달하여 JPAQueryFactory 객체를 생성 및 반환
}
