package ro.unicredit.trxclassifier.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKeywordWithOccurrencesDto extends ResponseKeywordDto{
    private Integer occurrences;

    public ResponseKeywordWithOccurrencesDto(ResponseKeywordDto responseKeywordDto, Integer occurrences) {
        super(responseKeywordDto.getId(), responseKeywordDto.getValue(), responseKeywordDto.getParent(), responseKeywordDto.getCategoryId());
        this.occurrences = occurrences;
    }
}
