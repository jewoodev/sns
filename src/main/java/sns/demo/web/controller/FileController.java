package sns.demo.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import sns.demo.domain.entity.File;
import sns.demo.web.service.FileService;

import java.net.MalformedURLException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class FileController {

    private final FileService fileService;

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable(name = "filename") String filename) throws MalformedURLException {
        File boardImage = null;
        if (fileService.findByFileName(filename).isPresent()) {
            boardImage = fileService.findByFileName(filename).get();
        }
        log.info("boardImage.url = {}", fileService.getFullPath(boardImage.getFilepath()));
        return new UrlResource("file:" + fileService.getFullPath(boardImage.getFilepath()));
    }
}
