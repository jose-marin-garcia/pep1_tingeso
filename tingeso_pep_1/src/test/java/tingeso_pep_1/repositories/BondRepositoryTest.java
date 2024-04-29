package tingeso_pep_1.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import tingeso_pep_1.entities.BondEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BondRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BondRepository bondRepository;

    /*@Test
    public void whenFindById_mark_thenReturnBond() {
        // given
        BondEntity bond = new BondEntity(null, 1L, 1000);
        entityManager.persistAndFlush(bond);

        // when
        BondEntity found = bondRepository.findById_mark(bond.getIdmark());

        // then
        assertThat(found.getIdmark()).isEqualTo(bond.getIdmark());
    }*/
}
