package ro.unicredit.trxclassifier.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKeywordWithOccurrencesDto extends ResponseKeywordDto{
    private Integer occurrences;

    public ResponseKeywordWithOccurrencesDto(ResponseKeywordDto responseKeywordDto, Integer occurrences) {
        super(responseKeywordDto.getId(), responseKeywordDto.getValue(), responseKeywordDto.getParent(), responseKeywordDto.getCategoryId());
        this.occurrences = occurrences;
    }
}
