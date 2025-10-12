package com.example.lifeshare.api.common.controller;

import com.example.lifeshare.api.common.model.CommonDto;
import com.example.lifeshare.api.common.model.RegionDto;
import com.example.lifeshare.api.common.service.CommonService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 공통 API 컨트롤러
 *
 * @author chauki
 * @version 1.0
 **/
@Tag(name = "공통 API")
@CrossOrigin
@RestController
@RequestMapping("/api/common")
public class CommonController {

    /**
     * CommonService 객체
     */
    private final CommonService commonService;

    /**
     * CommonController 클래스 생성자
     * - field Injection 방지
     *
     * @param commonService CommonService 객체
     * @author chauki
     * @version 1.0
     **/
    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    /**
     * 공통코드 조회 API
     *
     * @param codeType 대분류코드
     * @param grpType 중분류코드
     * @return List<CommonDto> 공통코드 목록
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "공통코드 조회 API (API-CMM-001)", description = "공통코드 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "codeType", description = " 대분류코드", required = true),
            @Parameter(name = "grpType", description = " 중분류코드", required = false)
    })
    @GetMapping("/code")
    @ResponseWrapper
    public List<CommonDto> reqGetCodeList(@RequestParam("codeType") String codeType,
                                          @RequestParam(value = "grpType", required = false) String grpType) throws ServiceException {
        return commonService.reqGetCodeList(codeType, grpType);
    }

    /**
     * 지역코드 조회 API(전체)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "지역코드 조회 API (API-CMM-002)", description = "지역코드 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "fulAdmNm", description = "풀 지역명", required = false),
    })
    @GetMapping("/region")
    @ResponseWrapper
    public List<RegionDto> reqGetRegionList(@RequestParam(value = "fulAdmNm", required = false) String fulAdmNm) throws ServiceException {
        return commonService.reqGetRegionList(fulAdmNm);
    }

    /**
     * 지역코드 조회 API(레벨 2까지)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "지역코드 조회 API (API-CMM-002)", description = "지역코드 정보를 레벨2까지 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "fulAdmNm", description = "풀 지역명", required = false),
    })
    @GetMapping("/region/lvl2")
    @ResponseWrapper
    public List<RegionDto> reqGetRegionListUpToLvl2(@RequestParam(value = "fulAdmNm", required = false) String fulAdmNm) throws ServiceException {
        return commonService.reqGetRegionListUpToLvl2(fulAdmNm);
    }

    /**
     * 지역코드 정보를 조회한다.(등록 카메라 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역코드 목록 정보
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "지역코드 조회 API (등록 카메라 주소 기준)", description = "지역코드 정보를 조회한다.(등록 카메라 주소 기준)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "fulAdmNm", description = "풀 지역명", required = false),
    })
    @GetMapping("/region/camera")
    @ResponseWrapper
    public List<RegionDto> reqGetRegionListForCameraInfo(@RequestParam(value = "fulAdmNm", required = false) String fulAdmNm) throws ServiceException {
        return commonService.reqGetRegionListForCameraInfo(fulAdmNm);
    }

    /**
     * 지역코드 정보를 조회한다.(등록 교차로 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "지역코드 조회 API (등록 교차로 주소 기준)", description = "지역코드 정보를 조회한다.(등록 교차로 주소 기준)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "fulAdmNm", description = "풀 지역명", required = false),
    })
    @GetMapping("/region/intersection")
    @ResponseWrapper
    public List<RegionDto> reqGetRegionListForIntersectionInfo(@RequestParam(value = "fulAdmNm", required = false) String fulAdmNm) throws ServiceException {
        return commonService.reqGetRegionListForIntersectionInfo(fulAdmNm);
    }
}
