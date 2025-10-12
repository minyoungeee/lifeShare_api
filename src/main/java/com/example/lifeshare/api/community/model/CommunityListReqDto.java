package com.example.lifeshare.api.community.model;

import com.example.lifeshare.cmm.dto.ListCommonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author : minyoung
 * @version : 1.0.0
 * @className : CommunityDto
 * @description : 공지사항 dto 클래스
 * @date : 2024-04-23 오후 5:01
 * @history :
 * @see
 **/

@Schema(description = "공지사항")
@Data
@SuperBuilder
public class CommunityListReqDto extends ListCommonDto{

    /**
     * 게시판 ID
     */
    @Schema(description = "게시판 ID", nullable = true)
    private String boardId;

    /**
     * 제목
     */
    @Schema(description = "제목", nullable = true)
    private String title;

    /**
     * 내용
     */
    @Schema(description = "내용", nullable = true)
    private String cont;

    /**
     * 등록일자
     */
    @Schema(description = "등록일자", nullable = true)
    private String regDt;

    /**
     * 카테고리 ID
     */
    @Schema(description = "카테고리 ID", nullable = true)
    private String ctgrId;

}

