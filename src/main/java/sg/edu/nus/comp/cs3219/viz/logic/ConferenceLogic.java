package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ConferenceLogic {

    private final ConferenceRepository conferenceRepository;

    public ConferenceLogic(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> findAllForUser(UserInfo userInfo) {
        List<Conference> result =  conferenceRepository.findByCreatorIdentifier(userInfo.getUserEmail());
        return result;
    }

    public Optional<Conference> findById(Long id) {
        return conferenceRepository.findById(id);
    }

    public List<Conference> findByUserInfoAndName(UserInfo userInfo, String name) {
        Optional<Conference> o;
        return conferenceRepository.findByCreatorIdentifierAndName(userInfo.getUserEmail(), name);
    }

    public List<Conference> findByUserAndName(String user, String name) {
        Optional<Conference> o;
        return conferenceRepository.findByCreatorIdentifierAndName(user, name);
    }


    public Conference saveForUser(Conference conference, UserInfo userInfo) {
        List<Conference> cList = conferenceRepository.findByCreatorIdentifierAndName(userInfo.getUserEmail(), conference.getName());
        if (cList.size() == 0) {
            Conference newConference = new Conference(userInfo.getUserEmail(), conference.getName());
            newConference.setDescription(conference.getDescription());
            newConference.setDate(conference.getDate());
            return conferenceRepository.save(newConference);
        }
        return cList.get(0);
    }

    public Conference updateConference(Conference oldConference, Conference newConference) {
        oldConference.setName(newConference.getName());
        oldConference.setDescription(newConference.getDescription());
        oldConference.setDate(newConference.getDate());
        return conferenceRepository.save(oldConference);
    }

    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }

    public List<Conference> findAllForUserWithRecordType(UserInfo userInfo, String recordType){
        if (recordType.equals("AuthorRecord")) {
            return conferenceRepository.findByCreatorIdentifierAndNumAuthorRecordGreaterThan(userInfo.getUserEmail(), 0);
        }
        if (recordType.equals("ReviewRecord")) {
            return conferenceRepository.findByCreatorIdentifierAndNumReviewRecordGreaterThan(userInfo.getUserEmail(), 0);
        }
        if (recordType.equals("SubmissionRecord")) {
            return conferenceRepository.findByCreatorIdentifierAndNumSubmissionRecordGreaterThan(userInfo.getUserEmail(), 0);
        } else {
            return null;
        }
//        return versionRepository.findById_DataSetAndId_RecordType(userInfo.getUserEmail(), recordType);
    }

    public List<Conference> findAllForUserWithVersion(UserInfo userInfo, String version){
        return conferenceRepository.findByCreatorIdentifierAndName(userInfo.getUserEmail(), version);
    }
}
