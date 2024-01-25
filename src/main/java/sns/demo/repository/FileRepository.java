package sns.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sns.demo.domain.UploadFile;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
}
