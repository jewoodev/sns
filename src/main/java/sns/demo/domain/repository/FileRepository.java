package sns.demo.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import sns.demo.domain.entity.File;
import sns.demo.web.service.BoardService;

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

    public Long save(File file) {
        em.persist(file);
        return file.getId();
    }

    public List<File> findAllByBoardId(Long boardId) {
        return em.createQuery("select f from File f where f.board = :board", File.class)
                .setParameter("board", boardService.findById(boardId))
                .getResultList();
    }

    public Optional<File> findByFileName(String filename) {

        Optional<File> optionalResult;

        try {
            File file = em.createQuery(
                            "select f from File f where f.filename = :filename", File.class)
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

    public List<File> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<File> storeImageResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImageResult.add(storeFile(multipartFile));
            }
        }
        return storeImageResult;
    }

    public File storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new java.io.File(fullPath));
        return new File(originalFilename, storeFileName);
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
