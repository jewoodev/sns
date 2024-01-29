package sns.demo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import sns.demo.domain.UploadFile;
import sns.demo.service.BoardService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;
    private final BoardService boardService;

    @Value("${file.dir}")
    private String fileDir;

    public Long save(UploadFile file) {
        em.persist(file);
        return file.getId();
    }

    public List<UploadFile> findAllByBoardId(Long boardId) {
        return em.createQuery("select f from UploadFile f where f.board = :board", UploadFile.class)
                .setParameter("board", boardService.findOne(boardId))
                .getResultList();
    }

    public UploadFile findByFileName(String filename) {
        return em.createQuery("select f from UploadFile f where f.filename = :filename", UploadFile.class)
                .setParameter("filename", filename)
                .getSingleResult();
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeImageResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImageResult.add(storeFile(multipartFile));
            }
        }
        return storeImageResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath));
        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
