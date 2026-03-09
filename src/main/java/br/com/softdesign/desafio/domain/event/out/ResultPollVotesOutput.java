package br.com.softdesign.desafio.domain.event.out;


import br.com.softdesign.desafio.domain.entity.Result;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultPollVotesOutput {

    @JsonProperty("CURRENT_VALUES")
    private ResultPollVotesDetailOutput currentValues;

    @JsonProperty("ORIGINAL_VALUES")
    private ResultPollVotesDetailOutput originalValues;

    public static ResultPollVotesOutput create(Result result) {
        ResultPollVotesDetailOutput detail = ResultPollVotesDetailOutput.builder()
                .poll(result.getPoll().getName())
                .countVotes(result.getCountVotes())
                .questions(result.getQuestions())
                .build();

        return ResultPollVotesOutput.builder()
                .currentValues(detail)
                .originalValues(detail)
                .build();
    }
}
