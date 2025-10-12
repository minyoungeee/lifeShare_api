package com.example.lifeshare.api.file.controller;

import com.example.lifeshare.api.file.model.FileDto;
import com.example.lifeshare.api.file.service.FileService;
import com.example.lifeshare.cmm.annotation.ResponseWrapper;
import com.example.lifeshare.cmm.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * FileController 클래스
 *
 * @author taejin
 * @version 1.0
 **/
@Tag(name = "파일 관리 API")
@CrossOrigin
@RequestMapping("/api/file")
@RestController
public class FileController {

    /**
     * FileService 객체
     */
    private final FileService fileService;

    /**
     * FileController 생성자
     * - Field Injection 방지
     *
     * @param fileService FileService 객체
     * @author taejin
     * @version 1.0
     **/
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * @funcName : reqGetFileInfo
     * @description : 파일 조회
     * @param fileDto : FileDto 객체
     * @return ResponseEntity<Resource> : 파일 정보(리소스)
     * @exception ServiceException : 예외
     * @date : 2024-05-02 오후 2:04
     * @author : minyoung
     * @see
     * @history :
     **/
    @Operation(summary = "파일 조회 API", description = "파일을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @GetMapping("")
    public ResponseEntity<Resource> reqGetFileInfo(@Parameter(name = "fileDto", description = "파일 정보 DTO") @RequestBody FileDto fileDto) throws ServiceException {
        return fileService.reqGetFileInfo(fileDto);
    }

    /**
     * @funcName : reqPostFileInfo
     * @description : 파일 다운로드
     * @param fileDto : FileDto 객체
     * @return ResponseEntity<Resource> : 파일 정보(리소스)
     * @exception ServiceException : 예외
     * @date : 2024-05-02 오후 2:04
     * @author : minyoung
     * @see
     * @history :
     **/
    @Operation(summary = "파일 다운로드 API", description = "파일을 다운로드한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @PostMapping("")
    public ResponseEntity<Resource> reqPostFileInfo(FileDto fileDto) throws ServiceException {
        return fileService.fileDownload(fileDto);
    }

   /**
    * @funcName : reqFileUpload
    * @description : 게시판 컨텐츠 내 파일 업로드
    * @param file : 파일정보
    * @return Map : 파일업로드 결과 정보
    * - 파일명
    * - 성공여부
    * - 파일 다운로드 url
    * @exception ServiceException : 예외
    * @date : 2024-05-02 오후 2:04
    * @author : minyoung
    * @see
    * @history :
    **/
   @Operation(summary = "게시판 컨텐츠 내 파일 업로드 API", description = "게시판 컨텐츠 내 파일을 업로드한다.")
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Success"),
           @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request"))),
           @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
           @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
           @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
   })
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map reqFileUpload(@RequestParam("upload") MultipartFile file) throws ServiceException {
        return fileService.reqPostContentFileUpload(file);
    };

    /**
     * 파일을 삭제한다.
     *
     * @param delFileList 파일 목록
     * @return 삭제성공여부
     * @author sylee
     * @version 1.0
     ** 1. 선택 조건에 대한 파일 삭제
     ** 2. 삭제 결과 반환
     **/
    @Operation(summary = "파일 삭제 API", description = "파일을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "delFileList", description = "삭제 파일 목록")
    })
    @DeleteMapping("")
    @ResponseWrapper
    public Boolean reqDeleteFileList(@RequestParam String delFileList) throws ServiceException {
        return fileService.reqDeleteFileList(delFileList);
    }
}