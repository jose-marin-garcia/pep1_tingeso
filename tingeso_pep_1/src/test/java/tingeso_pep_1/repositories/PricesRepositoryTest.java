package tingeso_pep_1.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import tingeso_pep_1.entities.PricesEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PricesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PricesRepository pricesRepository;

    @Test
    public void whenFindPriceByIdtyperepairAndMotortype_thenReturnPrice() {
        // given
        PricesEntity price = new PricesEntity(null, "Gasolina", 1L, 120000);
        entityManager.persistAndFlush(price);

        // when
        int found = pricesRepository.findPriceByIdtyperepairAndMotortype(price.getIdtyperepair(), price.getMotortype());

        // then
        assertThat(found).isEqualTo(price.getPrice());
    }

}
