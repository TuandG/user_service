package org.example.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageResponse<T>{
    private List<T> content;
    private int size;
    private int pageNumber;
    private int totalPage;

    public CustomPageResponse(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.totalPage = page.getTotalPages();
        this.pageNumber = page.getNumber();
    }
}
