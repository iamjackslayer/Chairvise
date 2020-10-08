package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.AuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ReviewRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionAuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionRecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecordLogic {
    private AuthorRecordRepository authorRecordRepository;

    private SubmissionRecordRepository submissionRecordRepository;

    private SubmissionAuthorRecordRepository submissionAuthorRecordRepository;

    private ReviewRecordRepository reviewRecordRepository;

    private ConferenceRepository conferenceRepository;

    public RecordLogic(AuthorRecordRepository authorRecordRepository,
                       SubmissionRecordRepository submissionRecordRepository,
                       SubmissionAuthorRecordRepository submissionAuthorRecordRepository,
                       ReviewRecordRepository reviewRecordRepository,
                       ConferenceRepository conferenceRepository) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.submissionAuthorRecordRepository = submissionAuthorRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
        this.conferenceRepository = conferenceRepository;
    }

    @Transactional
    public void removeAndPersistAuthorRecordForDataSet(String dataSet, List<AuthorRecord> authorRecordList) {
        if (authorRecordList.isEmpty()){
            return;
        }
        //authorRecordRepository.deleteAllByDataSetEquals(dataSet);
        // remove author entries of indicated version. This should effectively be 'updating' of author entries
        // It is of reasonable assumption that all records within the same upload are of the same version
        AuthorRecord temp = authorRecordList.get(0);
        // TODO: Change version with conference
        // Version v= new Version(new Version.VersionPK(dataSet, "AuthorRecord", temp.getVersion().getId().getVersion()));
        // Will only have 1 row at most.
        List<Conference> cList = conferenceRepository.findByCreatorIdentifierAndName(dataSet, temp.getConference().getName());
        Conference c;
        if (cList.size() > 0) {
            c = cList.get(0);
            c.setHasAuthorRecord(true);
        } else {
            c = new Conference(dataSet, temp.getConference().getName(), "AuthorRecord");
            conferenceRepository.save(c);
        }
        authorRecordRepository.deleteAllByConferenceEquals(c);
        authorRecordRepository.saveAll(authorRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setConference(c);
            //r.setDataSet(dataSet);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }

    @Transactional
    public void removeAndPersistReviewRecordForDataSet(String dataSet, List<ReviewRecord> reviewRecordList) {
        //reviewRecordRepository.deleteAllByDataSetEquals(dataSet);
        if (reviewRecordList.isEmpty()){
            return;
        }
        ReviewRecord temp = reviewRecordList.get(0);
        // TODO: Change version with conference
        List<Conference> cList = conferenceRepository.findByCreatorIdentifierAndName(dataSet, temp.getConference().getName());
        Conference c;
        if (cList.size() > 0) {
            c = cList.get(0);
            c.setHasReviewRecord(true);
        } else {
            c = new Conference(dataSet, temp.getConference().getName(), "ReviewRecord");
            conferenceRepository.save(c);
        }
        reviewRecordRepository.deleteAllByConferenceEquals(c);

        reviewRecordRepository.saveAll(reviewRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setConference(c);
            //r.setDataSet(dataSet);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }

    @Transactional
    public void removeAndPersistSubmissionRecordForDataSet(String dataSet, List<SubmissionRecord> submissionRecordList) {
        //submissionRecordRepository.deleteAllByDataSetEquals(dataSet);
        //submissionAuthorRecordRepository.deleteAllByDataSetEquals(dataSet);
        if (submissionRecordList.isEmpty()){
            return;
        }
        SubmissionRecord temp = submissionRecordList.get(0);
        System.out.println(temp.getConference().getName());
        // TODO: Change version with conference
        List<Conference> cList = conferenceRepository.findByCreatorIdentifierAndName(dataSet, temp.getConference().getName());
        Conference c;
        if (cList.size() > 0) {
            c = cList.get(0);
            c.setHasSubmissionRecord(true);
        } else {
            c = new Conference(dataSet, temp.getConference().getName(), "SubmissionRecord");
            conferenceRepository.save(c);
        }
        submissionRecordRepository.deleteAllByConferenceEquals(c);

        submissionRecordRepository.saveAll(submissionRecordList.stream().peek(s -> {
            // should not set ID when creating records
            s.setId(null);
            // should set dataSet
            s.setConference(c);
            //s.setDataSet(dataSet);
            // create many to many relationship for authors
            List<SubmissionAuthorRecord> submissionAuthorRecords = s.getAuthors().stream()
                    .map(authorName -> {
                        SubmissionAuthorRecord existing = submissionAuthorRecordRepository.findFirstByNameEqualsAndDataSetEquals(authorName, dataSet);
                        if (existing == null) {
                            existing = new SubmissionAuthorRecord();
                            existing.setDataSet(dataSet);
                            existing.setName(authorName);
                            existing = submissionAuthorRecordRepository.save(existing);
                        }
                        return existing;
                    })
                    .collect(Collectors.toList());
            s.setAuthorSet(submissionAuthorRecords);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }
}
