package com.example.lifeshare.api.community.mapper;

import com.example.lifeshare.api.community.model.CommunityDto;
import com.example.lifeshare.api.community.model.CommunityListReqDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : minyoung
 * @version : 1.0.0
 * @className : CommunityMapper
 * @description : 공지사항 정보 mapper 인터페이스
 * @date : 2024-04-24 오전 11:13
 * @history :
 * @see
 **/
@Mapper
public interface CommunityMapper {

    /**
     * @param communityListReqDto : CommunityListReqDto 객체
     * @return userDto : 채용 정보
     * @throws ServiceException : 예외
     * @funcName : selectListCommunity
     * @description : 공지사항 공고 목록 조회
     * @date : 2024-04-24 오전 11:13
     * @author : minyoung
     * @history :
     * @see
     **/
    List<CommunityDto> selectListCommunity(CommunityListReqDto communityListReqDto) throws ServiceException;

    /**
     * @param communityListReqDto : CommunityListReqDto 객체
     * @param ctgrId              : 카테고리 ID
     * @return : 공지사항 목록 개수 정보
     * @throws ServiceException : 예외
     * @funcName : selectOneCommunityCnt
     * @description : 공지사항 목록 갯수 조회
     * @date : 2024-04-24 오전 11:29
     * @author : minyoung
     * @history :
     * @see
     **/
    int selectOneCommunityCnt(CommunityListReqDto communityListReqDto, int ctgrId) throws ServiceException;

    /**
     * @funcName insertCommunityInfo:
     * @description : 공지사항 등록
     * @param communityDto : CommunityDto의 객체
     * @return Boolean : 등록 성공 여부
     * @exception ServiceException : 예외
     * @date : 2024-04-30 오후 3:15
     * @author : minyoung
     * @see
     * @history :
     **/
    int insertCommunityInfo(CommunityDto communityDto) throws ServiceException;

    /**
     * @funcName : reqPutCommunityInfo
     * @description : 공지사항 정보 수정
     * @param communityDto : CommunityDto 객체
     * @return : 공지사항 정보 수정 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-16 오전 9:48
     * @author : minyoung
     * @see
     * @history :
     **/
    int reqPutCommunityInfo(CommunityDto communityDto) throws ServiceException;
    
    /**
     * @funcName : reqDeleteCommunityInfo
     * @description : 공지사항 정보 삭제
     * @param communityIdList : communityIdList 객체
     * @return : 공지사항 정보 삭제 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-07 오전 9:30
     * @author : minyoung
     * @see
     * @history :
     **/
     int reqDeleteCommunityInfo(List<String> communityIdList) throws ServiceException;

    /**
     * @funcName : reqDeleteCommunityFileInfo
     * @description : 공지사항 파일 정보 삭제
     * @param communityIdList : communityIdList 객체
     * @return : 공지사항 파일 정보 삭제 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-07 오전 9:30
     * @author : minyoung
     * @see
     * @history :
     **/
    int reqDeleteCommunityFileInfo(List<String> communityIdList) throws ServiceException;

}
