package com.example.lifeshare.api.file.service;

import com.example.lifeshare.api.file.mapper.FileMapper;
import com.example.lifeshare.api.file.model.FileDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${file.community-path}")
    private String communityPath;

    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * 디렉토리 유무 체크 후, 없을 경우 생성
     */
    private void checkDirectory(String savePath) throws ServiceException {
        try {
            Path directoryPath = Paths.get(savePath).toAbsolutePath().normalize();
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            log.error("checkDirectory error for path={}", savePath, e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * DB 저장만 담당 (트랜잭션)
     */
    @Transactional
    protected FileDto saveFileMetaTransactional(String fileId,
                                                String originalFileName,
                                                String storedFileName,
                                                String contentType,
                                                String savePathWithTrailingSeparator,
                                                String videoYn,
                                                String typeNm) {
        try {
            if (fileId == null) return null;
            FileDto fileDto = new FileDto();
            fileDto.setRealFileNm(originalFileName);
            fileDto.setFileNm(storedFileName);
            fileDto.setExt(contentType);
            fileDto.setTargId(fileId);
            fileDto.setVideoYn(videoYn);
            fileDto.setFilePath(savePathWithTrailingSeparator); // ensure trailing separator for later concatenation
            fileDto.setTypeNm(typeNm);

            int result = fileMapper.reqPostFileInfo(fileDto);
            if (result > 0) {
                return fileDto;
            } else {
                return null;
            }
        } catch (DataAccessException dae) {
            log.error("saveFileMetaTransactional DB error", dae);
            throw dae;
        }
    }

    /**
     * 파일 업로드 (I/O + DB meta). 트랜잭션은 DB 메서드에서만 적용.
     */
    public FileDto fileUpload(MultipartFile file,
                              String fileId,
                              String savePath,
                              String videoYn,
                              String typeNm) throws ServiceException {
        if (savePath == null || savePath.isBlank()) {
            throw new ServiceException("저장할 파일경로가 없습니다.");
        }

        Path uploadDir;
        Path tmpPath = null;
        Path finalPath = null;
        String storedFileName = null;

        try {
            // 1) 디렉토리 준비 (절대 경로로 정규화)
            uploadDir = Paths.get(savePath).toAbsolutePath().normalize();
//            uploadDir = Paths.get(savePath);
            Files.createDirectories(uploadDir); // 존재하지 않으면 생성
            log.info("Resolved upload directory: {}", uploadDir.toString());

            // 2) 원본 파일명 안전 처리 및 저장 파일명 생성
            String originalFileName = (file.getOriginalFilename() == null) ? "unknown" : Paths.get(file.getOriginalFilename()).getFileName().toString();
            String ext = "";
            int idx = originalFileName.lastIndexOf('.');
            if (idx >= 0) ext = originalFileName.substring(idx);

            storedFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + ext;

             tmpPath = uploadDir.resolve(storedFileName + ".tmp");
             finalPath = uploadDir.resolve(storedFileName);

            log.info("Saving temporary upload to: {}", tmpPath.toString());

            // 3) 임시 저장
            file.transferTo(tmpPath.toFile());

            // 4) DB 메타 저장 (트랜잭션 내부)
            // ensure stored file path stored has trailing separator to match reqGetFileInfo usage
            String savePathWithSep = uploadDir.toString();
            if (!savePathWithSep.endsWith(File.separator)) savePathWithSep = savePathWithSep + File.separator;

            FileDto fileDto = saveFileMetaTransactional(fileId, originalFileName, storedFileName, file.getContentType(), savePathWithSep, videoYn, typeNm);
            if (fileDto == null) {
                // DB 저장 실패 -> 임시파일 삭제
                log.warn("DB meta insert failed, deleting tmp file: {}", tmpPath);
                Files.deleteIfExists(tmpPath);
                return null;
            }

            // 5) tmp -> final 이동
            Files.move(tmpPath, finalPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Moved uploaded file to final location: {}", finalPath.toString());

            return fileDto;

        } catch (IOException ioe) {
            log.error("File I/O error during upload", ioe);
            // cleanup tmp file if exists
            try { if (tmpPath != null) Files.deleteIfExists(tmpPath); } catch (Exception ex) { log.warn("Failed to cleanup tmp file", ex); }
            throw new ServiceException("파일 저장 실패: " + ioe.getMessage());
        } catch (DataAccessException dae) {
            log.error("DB error during file upload", dae);
            try { if (tmpPath != null) Files.deleteIfExists(tmpPath); } catch (Exception ex) { log.warn("Failed to cleanup tmp file", ex); }
            throw new ServiceException("DB 저장 실패: " + dae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in fileUpload", e);
            try { if (tmpPath != null) Files.deleteIfExists(tmpPath); } catch (Exception ex) { log.warn("Failed to cleanup tmp file", ex); }
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 파일 목록 조회
     */
    public List<FileDto> reqGetFileList(FileDto fileDto) throws ServiceException {
        try {
            return fileMapper.reqGetFileList(fileDto);
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 파일 조회 (리소스 반환)
     */
    public ResponseEntity<Resource> reqGetFileInfo(FileDto fileDto) throws ServiceException {
        try {
            FileDto tmpFileDto = fileMapper.reqGetFileInfo(fileDto);
            if (tmpFileDto != null) {
                String path = tmpFileDto.getFilePath() + tmpFileDto.getFileNm();
                Path filePath = Paths.get(path);
                if (Files.exists(filePath)) {
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource != null) {
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_TYPE, tmpFileDto.getExt())
                                .body(resource);
                    }
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException | MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 파일 다운로드
     */
    public ResponseEntity<Resource> fileDownload(FileDto fileDto) throws ServiceException {
        try {
            FileDto tmpFileDto = fileMapper.reqGetFileInfo(fileDto);
            if (tmpFileDto != null) {
                String path = tmpFileDto.getFilePath() + tmpFileDto.getFileNm();
                Path filePath = Paths.get(path);
                if (Files.exists(filePath)) {
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource != null) {
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_TYPE, tmpFileDto.getExt())
                                .header(HttpHeaders.SET_COOKIE, "fileDownload=true; path=/")
                                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", tmpFileDto.getRealFileNm()))
                                .body(resource);
                    }
                    return ResponseEntity.notFound().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException | MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 게시판 내 파일 업로드 (외부에서 호출되는 엔드포인트에서 사용)
     * - 트랜잭션은 내부 DB 저장 메서드에서만 적용됩니다.
     */
    public Map<String, Object> reqPostContentFileUpload(MultipartFile file) throws ServiceException {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            FileDto fileDto = this.fileUpload(file, "0", communityPath, "N", "");
            if (fileDto != null) {
                resultMap.put("filename", fileDto.getRealFileNm());
                resultMap.put("uploaded", "1");
                resultMap.put("url", "/api/file?fileId=" + fileDto.getFileId());
            } else {
                resultMap.put("uploaded", "0");
            }
            return resultMap;
        } catch (ServiceException e) {
            log.error("reqPostContentFileUpload error", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 파일 목록을 삭제한다.
     */
    @Transactional(rollbackFor = {RuntimeException.class})
    public Boolean reqDeleteFileList(String delFileList) throws ServiceException {
        try {
            for (String fileId : delFileList.split(",")) {
                if (!reqDeleteFileInfo(fileId))
                    return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException();
        }
    }

    /**
     * 파일을 삭제한다.
     */
    public Boolean reqDeleteFileInfo(String fileId) throws ServiceException {
        try {
            FileDto tmpFileDto = fileMapper.selectOneFileInfo(fileId);
            if (tmpFileDto == null) {
                return false;
            }
            int result = fileMapper.deleteFileInfo(fileId);
            if (result > 0) {
                if (tmpFileDto != null) {
                    String filePath = tmpFileDto.getFilePath() + tmpFileDto.getFileNm();
                    File file = new File(filePath);
                    if (file.exists()) {
                        fileDelete(file);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException();
        }
    }

    /**
     * 파일을 삭제한다. (synchronized로 TOCTOU 방지)
     */
    private synchronized void fileDelete(File file) {
        if (file != null) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (Exception e) {
                log.warn("fileDelete failed for {}", file.getAbsolutePath(), e);
            }
        }
    }
}
