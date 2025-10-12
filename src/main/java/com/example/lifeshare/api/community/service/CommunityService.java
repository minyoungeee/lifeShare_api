package com.example.lifeshare.api.community.service;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.example.lifeshare.api.community.mapper.CommunityMapper;
import com.example.lifeshare.api.community.model.CommunityDto;
import com.example.lifeshare.api.community.model.CommunityListReqDto;
import com.example.lifeshare.api.file.model.FileDto;
import com.example.lifeshare.api.file.service.FileService;
import com.example.lifeshare.cmm.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : minyoung
 * @version : 1.0.0
 * @className : CommunityService
 * @description :
 * @date : 2024-04-23 오후 5:35
 * @history :
 * @see
 **/
@Slf4j
@Service
public class CommunityService {

    @Value("${file.community-path}")
    private String communityPath;

    /**
     * CommunityMapper 객체
     */
    private final CommunityMapper communityMapper;

    /**
     * CommonApiService 객체
     */
    private final FileService fileService;

    public CommunityService(CommunityMapper communityMapper, FileService fileService) {
        this.communityMapper = communityMapper;
        this.fileService = fileService;
    }

    /**
     * @param limit        한 페이지당 요청 건수
     * @param pageNo       페이지 번호
     * @return : 공지사항 목록 정보
     * @throws ServiceException : 예외
     * @funcName : reqGetCommunityList
     * @description : 공지사항 목록 조회
     * @date : 2024-04-24 오전 10:27
     * @author : minyoung
     * @history :
     * @see
     **/
    public Map<String, Object> reqGetCommunityList(int limit,
                                                   int pageNo) throws ServiceException {
        try {
            Map<String, Object> resultMap = new HashMap<>();

            int ctgrId = 1;

            CommunityListReqDto communityListReqDto = CommunityListReqDto.builder()
                    .limit(limit)
                    .pageNo(pageNo)
                    .build();

            List<CommunityDto> communityList = communityMapper.selectListCommunity(communityListReqDto);


            for (CommunityDto item : communityList) {
                FileDto fileDto = new FileDto();
                fileDto.setTargId(item.getBoardId());
                List<FileDto> fileList = fileService.reqGetFileList(fileDto);

                item.setFileList(fileList);
            }

            resultMap.put("totalCnt", communityMapper.selectOneCommunityCnt(communityListReqDto, ctgrId));
            resultMap.put("communityList", communityList);

            return resultMap;

        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @funcName : reqPutCommunityInfo
     * @description : 공지사항 정보 수정
     * @param communityDto : CommunityDto 객체
     * @return : 공지사항 정보 수정 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-16 오전 9:53
     * @author : minyoung
     * @see
     * @history :
     **/
    @Transactional
    public Boolean reqPutCommunityInfo(CommunityDto communityDto,
                                       MultipartFile[] files) throws ServiceException {
        try {
            //xss 필터
            communityDto.setBoardId(XssPreventer.escape(communityDto.getBoardId()));
            communityDto.setTitle(XssPreventer.escape(communityDto.getTitle()));
//            communityDto.setCont(XssPreventer.escape(communityDto.getCont()));

            int result = communityMapper.reqPutCommunityInfo(communityDto);
            if (result > 0) {

                // 파일 등록
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        String originalFilename = files[i].getOriginalFilename();
                        if (originalFilename != null && !originalFilename.isEmpty()) {
                            fileService.fileUpload(files[i], communityDto.getBoardId(), "C:/NS_IMG/", "N", "community");
                        }
                    }
                }

                return true;
            }else {
                return false;
            }
        }catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @funcName : reqPostCommunityInfo
     * @description 공지사항 등록
     * @param communityDto : CommunityDto의 객체
     * @return Boolean 등록 성공 여부
     * @exception ServiceException : 예외
     * @date : 2024-04-30 오후 3:24
     * @author : minyoung
     * @see
     * @history :
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean reqPostCommunityInfo(CommunityDto communityDto, MultipartFile[] files) throws ServiceException {
        try {
            //xss 필터
            communityDto.setTitle(XssPreventer.escape(communityDto.getTitle()));
//            communityDto.setCont(XssPreventer.escape(communityDto.getCont()));
            communityDto.setRegDt(XssPreventer.escape(communityDto.getRegDt()));

            int result = communityMapper.insertCommunityInfo(communityDto);
            if (result > 0) {
                //file 파일 등록
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        fileService.fileUpload(files[i], communityDto.getBoardId(), communityPath, "N", "community");
                    }
                }
                return true;
            } else {
                return false;
            }

        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @funcName : reqDeleteCommunityInfo
     * @description : 공지사항 정보 삭제
     * @param fileId : 파일 아이디
     * @return : 공지사항 정보 삭제 여부
     * @exception ServiceException : 예외
     * @date : 2024-05-16 오후 2:52
     * @author : minyoung
     * @see
     * @history :
     **/
    @Transactional
    public Boolean reqDeleteCommunityInfo(List<String> communityIdList) throws ServiceException {
        try {
            int result = communityMapper.reqDeleteCommunityInfo(communityIdList);
            int fileResult = communityMapper.reqDeleteCommunityFileInfo(communityIdList);

            if (result > 0 && fileResult > 0) {
                return true;
            }else {
                return false;
            }
        }catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
