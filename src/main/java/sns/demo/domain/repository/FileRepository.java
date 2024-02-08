package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import sns.demo.domain.entity.FileEntity;
import sns.demo.web.service.BoardService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;
    private final BoardService boardService;

    @Value("${file.dir}")
    private String fileDir;

    public Long save(FileEntity file) {
        em.persist(file);
        return file.getId();
    }

    public List<FileEntity> findAllByBoardId(Long boardId) {
        return em.createQuery("select f from FileEntity f where f.boardEntity = :board", FileEntity.class)
                .setParameter("board", boardService.findOne(boardId))
                .getResultList();
    }

    public Optional<FileEntity> findByFileName(String filename) {

        Optional<FileEntity> optionalResult;

        try {
            FileEntity file = em.createQuery(
                            "select f from FileEntity f where f.filename = :filename", FileEntity.class)
                    .setParameter("filename", filename)
                    .getSingleResult();
            optionalResult = Optional.of(file);
        } catch (NoResultException e) {
            optionalResult = Optional.empty();
        }
        return optionalResult;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<FileEntity> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileEntity> storeImageResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImageResult.add(storeFile(multipartFile));
            }
        }
        return storeImageResult;
    }

    public FileEntity storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath));
        return new FileEntity(originalFilename, storeFileName);
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
