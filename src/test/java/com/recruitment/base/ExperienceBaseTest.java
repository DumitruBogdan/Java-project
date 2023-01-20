package com.recruitment.base;

import com.recruitment.domain.Experience;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.ExperienceDTO;
import org.springframework.mock.web.MockMultipartFile;

public class ExperienceBaseTest {

    protected final Long EXPERIENCE_ID = 1L;
    protected final String documentName = "document1.pdf";
    protected final DocumentType documentType = DocumentType.CV;
    protected Experience experienceId1;
    protected ExperienceDTO experienceDTO;
    protected ExperienceDTO experienceResponseDTO;
    protected MockMultipartFile document;

    public void init() {

        experienceDTO = ExperienceDTO.builder()
                .candidateId(1L)
                .cvName("document1")
                .comments("I work really hard!")
                .build();

        experienceResponseDTO = experienceDTO;

        experienceId1 = Experience.builder()
                .candidateId(1L)
                .cvName("document1")
                .comments("I work really hard!")
                .build();

        document = new MockMultipartFile(
                "uploaded-file",
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );
    }
}
