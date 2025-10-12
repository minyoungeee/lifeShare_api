package com.example.lifeshare.api.file.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 파일 MNG 정보 DTO
 *
 * @author TAEJIN
 * @version 1.0.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Schema(description = " 파일 MNG 정보")
@Data
public class FileManageDto {

    /**
     * 파일 아이디
     */
    private String fileId;

    /**
     * 타켓 아이디
     */
    private String targId;

    /**
     * 파일 타입
     */
    private String typeNm;

    /**
     * 파일 명
     */
    private String fileNm;

    /**
     * 파일 실제 명
     */
    private String realFileNm;

    /**
     * 파일 경로
     */
    private String filePath;

    /**
     * 파일 확장자
     */
    private String ext;

    /**
     * 영상파일 여부
     */
    private String videoYn;

    /**
     * 등록일자
     */
    private String regDt;

}