package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByCreatorIdentifier(String creatorIdentifier);

    List<Conference> findByCreatorIdentifierAndNumAuthorRecordGreaterThan(String creatorIdentifier, int numAuthorRecord);
    List<Conference> findByCreatorIdentifierAndNumReviewRecordGreaterThan(String creatorIdentifier, int numReviewRecord);
    List<Conference> findByCreatorIdentifierAndNumSubmissionRecordGreaterThan(String creatorIdentifier, int numSubmissionRecord);

    List<Conference> findByCreatorIdentifierAndName(String creatorIdentifier, String name);

}
