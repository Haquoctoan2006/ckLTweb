package vn.edu.ute.cklt_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.ute.cklt_web.dto.response.ApiResponse;
import vn.edu.ute.cklt_web.exception.BadRequestException;
import vn.edu.ute.cklt_web.service.CloudinaryService;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File không được để trống");
        }

        String url = cloudinaryService.uploadFile(file);
        return ApiResponse.success("Upload thành công", url);
    }
}
