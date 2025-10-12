package com.example.lifeshare.api.common.service;

import com.example.lifeshare.api.common.mapper.CommonMapper;
import com.example.lifeshare.api.common.model.CommonDto;
import com.example.lifeshare.api.common.model.RegionDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공통 API Service 클래스
 *
 * @author chauki
 * @version 1.0
 **/
@Slf4j
@Service
public class CommonService {

    /**
     * CommonMapper 객체
     */
    private final CommonMapper commonMapper;

    /**
     * @param commonMapper : CommonMapper 클래스
     * @return :
     * @throws :
     * @funcName : CommonService
     * @description : CommonService 클래스 생성자
     * @date : 2022-06-28 오전 10:34
     * @author : chauki
     * @history :
     * @see
     **/
    @Autowired
    public CommonService(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    /**
     * 공통코드 조회 API
     *
     * @param codeType 대분류코드
     * @param grpType  중분류코드
     * @return List<CommonDto> 공통코드 목록
     * @author chauki
     * @version 1.0
     **/
    public List<CommonDto> reqGetCodeList(String codeType,
                                          String grpType) throws ServiceException {
        try {
            CommonDto commonDto = new CommonDto();
            commonDto.setCodeType(codeType);
            commonDto.setGrpType(grpType);
            return commonMapper.selectListCodeInfo(commonDto);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 지역코드 조회 API(전체)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    public List<RegionDto> reqGetRegionList(String fulAdmNm) throws ServiceException {
        try {
            RegionDto regionDto = new RegionDto();
            regionDto.setFulAdmNm(fulAdmNm);
            return commonMapper.selectListRegionInfo(regionDto);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 지역코드 조회 API(레벨 2까지)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author parksujin
     * @version 1.0
     **/
    public List<RegionDto> reqGetRegionListUpToLvl2(String fulAdmNm) throws ServiceException {
        try {
            RegionDto regionDto = new RegionDto();
            regionDto.setFulAdmNm(fulAdmNm);
            return commonMapper.selectListRegionInfoUpToLvl2(regionDto);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 지역코드 정보를 조회한다.(등록 카메라 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역코드 목록 정보
     * @author chauki
     * @version 1.0
     **/
    public List<RegionDto> reqGetRegionListForCameraInfo(String fulAdmNm) throws ServiceException {
        try {
            return commonMapper.selectListRegionInfoForCamera(fulAdmNm);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 지역코드 정보를 조회한다.(등록 교차로 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    public List<RegionDto> reqGetRegionListForIntersectionInfo(String fulAdmNm) throws ServiceException {
        try {
            return commonMapper.selectListRegionInfoForIntersection(fulAdmNm);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
