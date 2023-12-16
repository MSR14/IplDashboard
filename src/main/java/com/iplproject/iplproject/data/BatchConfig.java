package com.iplproject.iplproject.data;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.iplproject.iplproject.model.Match;

@Configuration
public class BatchConfig {
    // @Autowired
    // public JobBuilder jobBuilder;
    // @Autowired
    // public StepBuilder stepBuilder;

    private final String[] FIELDS = new String[] {
            "ID", "City", "Date", "Season", "MatchNumber", "Team1", "Team2", "TossWinner", "TossDecision",
            "WinningTeam", "WonBy", "Margin", "method", "Player_of_Match", "Umpire1", "Umpire2"

    };

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("matchItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names(FIELDS)
                .targetType(MatchInput.class)
                .build();
    }

    @Bean
    public MatchDataProcessor processor() {
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Match>()
                .sql("INSERT INTO match (id, city, date, season, match_number, team1, team2, toss_winner, toss_decision, winning_team, won_by, win_margin, method, player_of_match, umpire1, umpire2) VALUES (:id, :city, :date, :season, :matchNumber, :team1, :team2, :tossWinner, :tossDecision, :winningTeam, :wonBy, :winMargin, :method, :playerOfMatch, :umpire1, :umpire2)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            FlatFileItemReader<MatchInput> reader, MatchDataProcessor processor, JdbcBatchItemWriter<Match> writer) {
        return new StepBuilder("step1", jobRepository)
                .<MatchInput, Match>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
