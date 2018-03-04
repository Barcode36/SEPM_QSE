package main.java.ac.at.tuwien.sepm.QSE15.test;

import main.java.ac.at.tuwien.sepm.QSE15.dao.statisticsDAO.JDBCStatisticsDAO;
import main.java.ac.at.tuwien.sepm.QSE15.service.statistics.StatisticServiceIMPL;
import org.junit.Before;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

/**
 * Created by Stefan Puhalo on 6/21/2017.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class StatisticsServiceTest extends AbstractStatisticsServiceTest {

    private AnnotationConfigApplicationContext context;

    private JDBCStatisticsDAO statisticsDAO;

    private StatisticServiceIMPL statisticService;


    @Before
    public void setUp() throws SQLException {

        context = new AnnotationConfigApplicationContext(this.getClass());

        statisticService = context.getBean(StatisticServiceIMPL.class);

        statisticsDAO = mock(JDBCStatisticsDAO.class);
        setStatisticsDAO(statisticsDAO);
        setStatisticsService(statisticService);

    }


}
