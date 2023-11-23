package com.joseleonardo.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joseleonardo.data.dto.v1.UploadFileResponseDTO;
import com.joseleonardo.services.FileStorageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/files/v1")
@Tag(name = "File Endpoint")
public class FileController {

	private Logger logger = Logger.getLogger(FileController.class.getName());
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/upload-file")
	public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("Armazenando arquivo em disco");
		
		String filename = fileStorageService.storeFile(file);
		
		String filedDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/files/v1/download-file/")
		        .path(filename)
				.toUriString();
		
		return new UploadFileResponseDTO(
				filename, filedDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/upload-multiple-files")
	public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		logger.info("Armazenando vários arquivo em disco");
		
		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}
	
}
