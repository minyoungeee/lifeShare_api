package com.example.lifeshare.api.file.mapper;

import com.example.lifeshare.api.file.model.FileDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * FileMapper 클래스
 *
 * @author minyoung
 * @version 1.0
 **/
@Mapper
public interface FileMapper {

    /**
     * @funcName : reqGetFileList
     * @description : 파일 목록 조회
     * @param fileDto : FileDto 객체
     * @return List<FileDto> : 파일 목록 정보
     * @exception ServiceException : 예외
     * @date : 2024-05-02 오전 11:43
     * @author : minyoung
     * @see
     * @history :
     **/
    List<FileDto> reqGetFileList(FileDto fileDto) throws ServiceException;

    /**
     * @funcName : reqGetFileInfo
     * @description : 파일 정보 조회
     * @param fileDto : FileDto 객체
     * @return FileDto : FileDto 객체
     * @exception ServiceException : 예외
     * @date : 2024-05-02 오전 11:45
     * @author : minyoung
     * @see
     * @history :
     **/
    FileDto reqGetFileInfo(FileDto fileDto) throws ServiceException;

    /**
     * @funcName : reqPostFileInfo
     * @description : 파일 정보 등록
     * @param fileDto : FileDto 객체
     * @return int: 파일 등록 성공여부
     * @exception ServiceException : 예외
     * @date : 2024-05-02 오전 11:46
     * @author : minyoung
     * @see
     * @history :
     **/
    int reqPostFileInfo(FileDto fileDto) throws ServiceException;

    /**
     * 파일 정보를 조회한다.
     *
     * @param fileId 첨부파일아이디
     * @return FileDto 파일 정보
     * @author chauki
     * @version 1.0
     ** 1. 파일 정보 조회 결과 반환
     **/
    FileDto selectOneFileInfo(String fileId) throws ServiceException;

    /**
     * 첨부파일 정보를 삭제한다.
     *
     * @param fileId 첨부파일 아이디
     * @return int 첨부파일 삭제 성공 여부
     * @author sylee
     * @version 1.0
     ** 1. 첨부파일 정보 삭제 결과 반환
     **/
    int deleteFileInfo(String fileId) throws ServiceException;

}