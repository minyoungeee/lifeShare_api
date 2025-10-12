package com.example.lifeshare.api.community.controller;

import com.example.lifeshare.api.community.model.CommunityDto;
import com.example.lifeshare.api.community.service.CommunityService;
import com.example.lifeshare.cmm.annotation.ResponseWrapper;
import com.example.lifeshare.cmm.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @className : CommunityController
 * @description : community api controller
 * @date : 2024-04-23 오후 5:25
 * @author : minyoung
 * @version : 1.0.0
 * @see
 * @history :
 **/
@CrossOrigin
@RequestMapping("/api/community")
@RestController
public class CommunityController {

    /**
     * CommunityService 객체
     */
    private final CommunityService communityService;

    /**
     * CommunityController 생성자 - Field Injection 방지
     *
     * @param communityService CommunityService 객체
     * @author minyoung
     * @version 1.0
     */
    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    /**
     * @funcName : reqGetCommunityList
     * @description : 공지사항 목록 조회
     * @param  limit :  한 페이지에 보여줄 목록 갯수
     * @param  pageNo : 페이지 번호
     * @return : 공지사항 목록 정보
     * @exception ServiceException : 예외
     * @date : 2024-04-24 오전 11:23
     * @author : minyoung
     * @see
     * @history :
     **/
    @Operation(summary = "공지사항 목록 조회 API", description = "공지사항 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "limit", description = "한페이지에 보여줄 목록 갯수", required = false),
            @Parameter(name = "pageNo", description = "페이지 번호", required = false),
    })
    @GetMapping("")
    @ResponseWrapper
    public Map reqGetCommunityList(@RequestParam(required = false, defaultValue = "0") int limit,
                                   @RequestParam(required = false, defaultValue = "0") int pageNo) throws ServiceException {
        return communityService.reqGetCommunityList(limit, pageNo);
    }

    /**
     * @funcName : reqPutCommunityInfo
     * @description : 공지사항 수정
     * @param  :
     * @return :
     * @exception ServiceException : 예외
     * @date : 2024-05-16 오전 9:51
     * @author : minyoung
     * @see
     * @history :
     **/
    @Operation(summary = "공지사항 수정 API", description = "공지사항을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "boardId", description = "공지사항 아이디", required = false),
            @Parameter(name = "title", description = "제목", required = false),
            @Parameter(name = "cont", description = "내용", required = false),
    })
    @PutMapping("")
    @ResponseWrapper
    public Boolean reqPutCommunityInfo(@ModelAttribute CommunityDto communityDto,
                                       @RequestParam(value = "files", required = false) MultipartFile[] files) throws ServiceException {
        return communityService.reqPutCommunityInfo(communityDto, files);
    }

    /**
     * @funcName : reqPostCommunityInfo
     * @description : 공지사항 정보 등록
     * @param communityDto : CommunityDto 객체
     * @return Boolean 등록 성공 여부
     * @exception ServiceException : 예외
     * @date : 2024-04-30 오후 3:32
     * @author : minyoung
     * @see
     * @history :
     **/
    @Operation(summary = "공지사항 등록 API", description = "이웃피드 정보를 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "files", description = "파일", schema = @Schema(accessMode = Schema.AccessMode.READ_ONLY))
    })
    @PostMapping("")
    @ResponseWrapper
    public Boolean reqPostCommunityInfo(@ModelAttribute CommunityDto communityDto,
                                        @RequestParam(value = "files", required = false) MultipartFile[] files) throws ServiceException {
        return communityService.reqPostCommunityInfo(communityDto, files);
    }

    /**
     * @funcName : reqDeleteCommunityInfo
     * @description : 공지사항 정보 삭제
     * @param communityIdList : communityIdList 객체
     * @return : 공지사항 정보 삭제 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-16 오후 2:54
     * @author : minyoung
     * @see
     * @history :
     **/
    @DeleteMapping("")
    @ResponseWrapper
    public Boolean reqDeleteCommunityInfo(@RequestParam(value = "boardId", required = false) List<String> communityIdList) throws ServiceException {

        return communityService.reqDeleteCommunityInfo(communityIdList);
    }
}
