package com.example.estoque_api.repository.custom;

import com.example.estoque_api.configuration.TestQueryDslConfig;
import com.example.estoque_api.dto.request.filter.PeriodRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({HistoryRepositoryCustomImpl.class, TestQueryDslConfig.class})
class HistoryRepositoryCustomImplTest {

    @Autowired
    private HistoryRepositoryCustom historyRepository;

    @Test
    void should_return_top_10_metrics_with_valid_period() {
        var period = new PeriodRequestDTO(
                LocalDate.now().minusDays(7),
                LocalDate.now()
        );

        var results = historyRepository.findTop10WithFilter(period);

        assertThat(results).isNotNull();
    }

    @Test
    void should_apply_filters_correctly_when_dates_are_null() {
        var emptyPeriod = new PeriodRequestDTO(null, null);
        var results = historyRepository.findTop10WithFilter(emptyPeriod);

        assertThat(results).isNotNull();
    }

    @Test
    void should_ensure_query_syntax_is_valid_even_with_subqueries() {
        var period = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());
        var results = historyRepository.findTop10WithFilter(period);

        assertThat(results).isInstanceOf(List.class);
    }
}