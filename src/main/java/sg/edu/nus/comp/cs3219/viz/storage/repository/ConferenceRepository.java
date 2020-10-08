package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByCreatorIdentifier(String creatorIdentifier);

    List<Conference> findByCreatorIdentifierAndHasAuthorRecord(String creatorIdentifier, boolean hasAuthorRecord);
    List<Conference> findByCreatorIdentifierAndHasReviewRecord(String creatorIdentifier, boolean hasReviewRecord);
    List<Conference> findByCreatorIdentifierAndHasSubmissionRecord(String creatorIdentifier, boolean hasSubmissionRecord);

    List<Conference> findByCreatorIdentifierAndName(String creatorIdentifier, String name);

}
