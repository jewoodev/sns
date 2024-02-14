package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sns.demo.domain.entity.File;
import sns.demo.domain.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    public String getFullPath(String filename) {
        return fileRepository.getFullPath(filename);
    }

    @Transactional
    public Long upload(File file) {
        return fileRepository.save(file);
    }

    public List<File> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        return fileRepository.storeFiles(multipartFiles);
    }

    @Transactional(readOnly = true)
    public Optional<File> findByFileName(String filename) {
        return fileRepository.findByFileName(filename);
    }

    @Transactional(readOnly = true)
    public List<File> findAllByBoardId(Long boardId) {
        return fileRepository.findAllByBoardId(boardId);
    }
}
