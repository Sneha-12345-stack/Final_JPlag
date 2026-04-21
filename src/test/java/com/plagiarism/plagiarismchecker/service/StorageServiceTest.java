
package com.plagiarism.plagiarismchecker.service;

import com.plagiarism.plagiarismchecker.entity.Job;
import com.plagiarism.plagiarismchecker.entity.User;
import com.plagiarism.plagiarismchecker.repository.UserRepository;
import com.plagiarism.plagiarismchecker.repository.JobRepository;
import com.plagiarism.plagiarismchecker.repository.SubmissionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StorageServiceTest {

    @Autowired
    private StorageService storageService;

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;

    


    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    void testStoreZipAndExtraction() throws Exception {

    	// ✅ 1. Create User
    	User user = new User();
    	user.setName("Sneha");
    	user.setEmail("test" + System.currentTimeMillis() + "@mail.com");
    	user.setPassword("1234");
    	user.setRole("STUDENT");

    	User savedUser = userRepository.save(user);

    	// ✅ 2. Create Job
    	Job job = new Job();
    	job.setJobName("Test Job");
    	job.setDescription("Storage Test");
    	job.setUser(savedUser);   // ⭐ IMPORTANT

    	Job savedJob = jobRepository.save(job);
     

        // ✅ 3. Create REAL ZIP in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        zos.putNextEntry(new ZipEntry("Main.java"));
        zos.write("public class Main {}".getBytes());
        zos.closeEntry();
        zos.close();

        MockMultipartFile zipFile = new MockMultipartFile(
                "file",
                "test.zip",
                "application/zip",
                baos.toByteArray()
        );

        // ✅ 3. Call Storage Service
        storageService.storeZip(savedJob.getId(), "Sneha", zipFile);

        // ✅ 4. Verify DB entry
        assertFalse(submissionRepository.findAll().isEmpty());

        // ✅ 5. Verify folder created
        Path path = storageService.getSubmissionPath(savedJob.getId())
                .resolve("Sneha");

        assertTrue(Files.exists(path));

        // ✅ 6. Verify file extracted
        Path extractedFile = path.resolve("Main.java");
        assertTrue(Files.exists(extractedFile));
    }
}