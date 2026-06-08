package com.ntq.demo.common.util;
import com.ntq.demo.common.constant.Constants;

import com.ntq.demo.exception.InvalidFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * This class is used to implement common functions that related to Files
 *
 * @author Quang
 * @since 2026-4-28
 */
public class FileHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
	private static final Tika TIKA = new Tika();

	/**
	 * Check if request file has the right type in 1 of 5: png, jpg, jpeg, webp, gif
	 *
	 * @param file
	 * @return true if right, otherwise false
	 */
	public static boolean isImageFile(MultipartFile file) {
		if (file == null || file.isEmpty()) return false;
		try {
			/**
			 * Tika read magic bytes from stream, not loading all the file into RAM
			 */
			String mimeType = TIKA.detect(file.getInputStream());
			LOGGER.info("Detected MIME type: {}", mimeType);
			return Constants.ALLOWED_IMAGE_TYPES.contains(mimeType);
		} catch (IOException e) {
			LOGGER.error("Failed to detect file type: {}", e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Save new file, delete old file if exists
	 * Throw InvalidFileException if: cannot create folder/save file fail
	 *
	 * @param folderPath
	 * @param files
	 * @param oldPath = path to image of record in DB, for example: brand.getLogo()
	 * @return new file path
	 * @throws InvalidFileException if file operations fail
	 */
	public static String editFile(String folderPath, MultipartFile[] files, String oldPath) {
		/**
		 * Not has new file -> Keep the old file path
		 */
		if (files == null || files.length == 0 || files[0].getSize() == 0) {
			return oldPath;
		}

		MultipartFile file = files[0];

		/**
		 * Check if image file or not, throw InvalidFileException
		 */
		if (!isImageFile(file)) {
			LOGGER.warn("Rejected non-image file: {}", file.getOriginalFilename());
			throw new InvalidFileException("File must be a valid image (png, jpg, jpeg, webp, gif)");
		}

		try {
			/**
			 * Create new folder if not exists
			 * Throw InvalidFileException with root cause IOException if IOException
			 */
			Path folder = Paths.get(folderPath);
			if (!Files.exists(folder)) {
				try {
					Files.createDirectories(folder);
					LOGGER.info("Created folder: {}", folderPath);
				} catch (IOException e) {
					throw new InvalidFileException(
						"Failed to create upload folder. Please check file permissions", e);
				}
			}

			/**
			 * Create unique file name
			 */
			String originalName = file.getOriginalFilename();
			String extension = getExtension(originalName);
			String newFileName = UUID.randomUUID() + extension;
			Path savePath = folder.resolve(newFileName);

			/**
			 * Save new file, replace the old file if exists
			 * Throw InvalidFileException with root cause IOException if IOException
			 */
			try {
				Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
				LOGGER.info("Saved file: {}", savePath);
			} catch (IOException e) {
				throw new InvalidFileException(
						"Failed to save file to disk. Please check disk space and file permissions", e);
			}

			/**
			 * Delete old file (if exists)
			 */
			deleteFile(oldPath);
			return folderPath + newFileName;
		} catch (InvalidFileException e) {
			throw e;
		} catch (Exception e) {
			/**
			 * Wrap unexpected exception into InvalidFileException
			 */
			LOGGER.error("Error while saving file: {}", e.getMessage(), e);
			throw new InvalidFileException("Error while saving file", e);
		}
	}

	/**
	 * Delete the file according to file path
	 * Log, no throw because delete file is not critical (database was updated)
	 *
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		if (filePath == null || filePath.isBlank()) return;
		try {
			Path path = Paths.get(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				LOGGER.info("Deleted file: {}", filePath);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to delete file {}: {}", filePath, e.getMessage(), e);
			throw new InvalidFileException("Failed to delete old file: " + filePath, e);
		}
	}

	/**
	 *  Get file extension name
	 *
	 * @param fileName
	 * @return File extension name (.jpg, .png,...)
	 */
	private static String getExtension(String fileName) {
		if (fileName == null || !fileName.contains(".")) return "";
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
