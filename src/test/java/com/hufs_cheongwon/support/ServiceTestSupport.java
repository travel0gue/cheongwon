package com.hufs_cheongwon.support;

import com.hufs_cheongwon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

/**
 * Service 계층 테스트를 위한 공통 Support 클래스
 * 
 * 사용법:
 * - Service 테스트 클래스에서 이 클래스를 상속받아 사용
 * - 필요한 Repository들이 자동으로 주입됨
 * - TestEntityManager를 통해 데이터 플러시 및 관리 가능
 * 
 * 예시:
 * class MyServiceTest extends ServiceTestSupport {
 *     @Test
 *     void testMethod() {
 *         Users user = usersRepository.save(UserFixture.createActiveUser());
 *         flushAndClear();
 *         // 테스트 로직
 *     }
 * }
 */
@DataJpaTest
@ActiveProfiles("test")
public abstract class ServiceTestSupport {

    @Autowired
    protected TestEntityManager entityManager;

    // 주요 Repository들
    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected PetitionRepository petitionRepository;

    @Autowired
    protected AdminRepository adminRepository;

    @Autowired
    protected ResponseRepository responseRepository;

    @Autowired
    protected AgreementRepository agreementRepository;

    @Autowired
    protected ReportRepository reportRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected PetitionBookmarkRepository petitionBookmarkRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected BlackListRepository blackListRepository;

    /**
     * 엔티티 매니저 플러시 및 클리어
     * 데이터베이스에 변경사항을 반영하고 영속성 컨텍스트를 비움
     */
    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 엔티티 매니저 플러시
     * 데이터베이스에 변경사항을 반영
     */
    protected void flush() {
        entityManager.flush();
    }

    /**
     * 영속성 컨텍스트 클리어
     * 1차 캐시를 비워서 다음 조회시 데이터베이스에서 새로 가져옴
     */
    protected void clear() {
        entityManager.clear();
    }

    /**
     * 엔티티를 영속성 컨텍스트에서 분리
     * @param entity 분리할 엔티티
     */
    protected void detach(Object entity) {
        entityManager.detach(entity);
    }

    /**
     * 엔티티를 영속성 컨텍스트에 병합
     * @param entity 병합할 엔티티
     * @return 병합된 엔티티
     */
    protected <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * 엔티티를 영속성 컨텍스트에서 새로고침
     * @param entity 새로고침할 엔티티
     */
    protected void refresh(Object entity) {
        entityManager.refresh(entity);
    }
}