package com.example.lifeshare.api.common.mapper;

import com.example.lifeshare.api.common.model.CommonDto;
import com.example.lifeshare.api.common.model.RegionDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 공통 API Mapper 클래스
 *
 * @author chauki
 * @version 1.0
 **/
@Repository
public interface CommonMapper {

    /**
     * 공통코드 목록을 조회한다.
     *
     * @param commonDto CommonDto 객체
     * @return List<CommonDto> 공통코드 목록 정보
     * @author chauki
     * @version 1.0
     **/
    List<CommonDto> selectListCodeInfo(CommonDto commonDto) throws ServiceException;

    /**
     * 지역코드 목록을 조회한다.
     *
     * @param regionDto RegionDto 객체
     * @return List<RegionDto> 지역코드 목록 정보
     * @author chauki
     * @version 1.0
     **/
    List<RegionDto> selectListRegionInfo(RegionDto regionDto) throws ServiceException;

    /**
     * 지역코드 조회 API(레벨 2까지)
     *
     * @param regionDto RegionDto 객체
     * @return  List<RegionDto> 지역코드 목록 정보
     * @author parksujin
     * @version 1.0
     **/
    List<RegionDto> selectListRegionInfoUpToLvl2(RegionDto regionDto) throws ServiceException;

    /**
     * 지역코드 정보를 조회한다.(등록 카메라 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    List<RegionDto> selectListRegionInfoForCamera(@Param("fulAdmNm") String fulAdmNm) throws ServiceException;

    /**
     * 지역코드 정보를 조회한다.(등록 교차로 주소 기준)
     *
     * @param fulAdmNm 풀 지역명
     * @return List<RegionDto> 지역정보
     * @author chauki
     * @version 1.0
     **/
    List<RegionDto> selectListRegionInfoForIntersection(@Param("fulAdmNm") String fulAdmNm) throws ServiceException;
}
